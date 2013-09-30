package org.pg.codecity;

import com.habelitz.jsobjectizer.unmarshaller.antlrbridge.generated.JavaLexer;
import com.habelitz.jsobjectizer.unmarshaller.antlrbridge.generated.JavaParser;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class JavaClass implements TreeVisitorAction {

    private enum ClassType {CLASS, INTERFACE, ENUM}

    private JavaClass() {};

    String _package;
    String _className;
    ClassType _classType;
    List<String> importList = new ArrayList<String>();

    public static JavaClass parseCommonTree(File file) throws Exception {
        JavaLexer lexer = new JavaLexer(new ANTLRInputStream(new FileInputStream(file)));
        JavaParser parser = new JavaParser(new CommonTokenStream(lexer));
        CommonTree tree = parser.javaSource().getTree();
        JavaClass _class = new JavaClass();
        new TreeVisitor().visit(tree, _class);
        return _class;
    }

    public void printTree(Object o, String indent) {
        CommonTree commonTree = (CommonTree) o;
        System.out.println(indent + commonTree.toString());
        if (commonTree.getChildCount() > 0) {
            for (CommonTree ct : (List<CommonTree>) commonTree.getChildren()) {
                printTree(ct, indent + " ");
            }
        }
    }

    @Override
    public Object pre(Object o) {
        CommonTree tree = (CommonTree) o;
        if (tree.getText().equals("package")) {
            _package = getPathRec(tree.getChild(0));
            return null;
        } else if (tree.getText().equals("import")) {
            if (tree.getChild(0).getText().equals("static")) {
                importList.add(getPathRec(tree.getChild(1)));
            } else {
                importList.add(getPathRec(tree.getChild(0)));
            }
            return null;
        } else if (tree.getText().equals("class")) {
            if (hasChildrenToken(tree, "CLASS_TOP_LEVEL_SCOPE")) {
                _classType = ClassType.CLASS;
                _className = tree.getChild(1).getText();
            }
        } else if (tree.getText().equals("interface")) {
            if (hasChildrenToken(tree, "CLASS_TOP_LEVEL_SCOPE")) {
                _classType = ClassType.INTERFACE;
                _className = tree.getChild(1).getText();
            }
        } else if (tree.getText().equals("enum")) {
            if (hasChildrenToken(tree, "ENUM_TOP_LEVEL_SCOPE")) {
                _classType = ClassType.ENUM;
                _className = tree.getChild(1).getText();
            }
        }

        return o;
    }

    private boolean hasChildrenToken(CommonTree tree, String tokenName) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            if (child.getText().equals(tokenName)) {
                return true;
            }
        }

        return false;
    }

    private String getPathRec(Tree ct) {
        if (ct.getText().equals(".")) {
            return getPathRec(ct.getChild(0)) + "." + ct.getChild(1);
        } else {
            return ct.getText();
        }
    }

    @Override
    public Object post(Object o) {
        return o;
    }

    public void dump() {
        System.out.println(getFullyQualifiedClassName());
        if (!importList.isEmpty()) {
            System.out.println("imports: ");
            for (String _import : importList) {
                System.out.println("  " + _import);
            }
        }
    }

    private String getFullyQualifiedClassName() {
        return _package + "." + _className + " (" +_classType +")";
    }
}
