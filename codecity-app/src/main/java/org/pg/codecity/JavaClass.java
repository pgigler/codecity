package org.pg.codecity;

import com.habelitz.jsobjectizer.unmarshaller.antlrbridge.generated.JavaLexer;
import com.habelitz.jsobjectizer.unmarshaller.antlrbridge.generated.JavaParser;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.List;

public class JavaClass implements TreeVisitorAction {
    String _package;
    String _className;
    List<String> importList = new ArrayList<String>();

    public static JavaClass parseCommonTree(String fileName) throws Exception {
        JavaLexer lexer = new JavaLexer(new ANTLRFileStream(fileName));
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
        } else if (tree.getText().equals("import")) {
            importList.add(getPathRec(tree.getChild(0)));
        } else if (tree.getText().equals("class")) {
            _className = tree.getChild(1).getText();
        }

        return o;
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
        return _package + "." + _className;
    }
}
