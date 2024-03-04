import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Test implements Serializable{
    private TreeNode root;
    private String name;

    public Test(String name) {
    	this.name = name;
        this.root = null;
    }

    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        inOrderTraversal(root, questions);
        return questions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void inOrderTraversal(TreeNode node, List<Question> questions) {
        if (node != null) {
            inOrderTraversal(node.leftChild, questions); // Traverse left subtree
            questions.add(node.question); // Visit node
            inOrderTraversal(node.rightChild, questions); // Traverse right subtree
        }
    }

    public void postOrderTraversal(TreeNode node, List<Question> questions) {
        if (node != null) {
            postOrderTraversal(node.leftChild, questions); // Traverse left subtree
            postOrderTraversal(node.rightChild, questions); // Traverse right subtree
            questions.add(node.question); // Visit node
        }
    }


    // Method to add a question to the binary search tree
    public void addQuestion(Question question) {
        root = insertRecursive(root, question);
    }

    public void addQuestions(List<Question> questions) {
        for (Question question : questions) {
            addQuestion(question);
        }
    }

    // Recursive method to add a question to the binary search tree
    private TreeNode insertRecursive(TreeNode node, Question question) {
        if (node == null) {
            return new TreeNode(question);
        }

        // Compare question names to decide where to insert the new node
        if (question.getName().compareTo(node.question.getName()) < 0) {
            node.leftChild = insertRecursive(node.leftChild, question);
        } else if (question.getName().compareTo(node.question.getName()) > 0) {
            node.rightChild = insertRecursive(node.rightChild, question);
        }

        return node;
    }

    public TreeNode getRootNode() {
        // Return the root node of the tree
        return this.root;
    }


    // Method to remove a question from the binary search tree
    private void removeQuestion(int difficulty) {
        root = removeQuestionRecursive(root, difficulty);
    }

    // Recursive method to remove a question from the binary search tree
    private TreeNode removeQuestionRecursive(TreeNode node, int difficulty) {
        if (node == null) {
            return null;
        }

        if (difficulty < node.question.getDifficulty()) {
            node.leftChild = removeQuestionRecursive(node.leftChild, difficulty);
        } else if (difficulty > node.question.getDifficulty()) {
            node.rightChild = removeQuestionRecursive(node.rightChild, difficulty);
        } else {
            // Case 1: No children or only one child
            if (node.leftChild == null) {
                return node.rightChild;
            } else if (node.rightChild == null) {
                return node.leftChild;
            }

            // Case 2: Two children
            // Replace the node's value with the minimum value from the right subtree
            node.question = minValue(node.rightChild);

            // Remove the node with the minimum value from the right subtree
            node.rightChild = removeQuestionRecursive(node.rightChild, node.question.getDifficulty());
        }

        return node;
    }
    public boolean searchQuestionByName(String name) {
        return searchRecursive(root, name);
    }

    private boolean searchRecursive(TreeNode node, String name) {
        // Base case: if the node is null, the question is not found
        if (node == null) {
            return false;
        }

        // Compare the search name with the current node's question name
        int comparison = name.compareTo(node.question.getName());

        // If the names match, the question is found
        if (comparison == 0) {
            return true;
        }

        // If the search name is less than the current node's name, search the left subtree
        if (comparison < 0) {
            return searchRecursive(node.leftChild, name);
        } else {
            // Otherwise, search the right subtree
            return searchRecursive(node.rightChild, name);
        }
    }
    // Method to find the node with the minimum value in a subtree
    private Question minValue(TreeNode node) {
        Question minv = node.question;
        while (node.leftChild != null) {
            minv = node.leftChild.question;
            node = node.leftChild;
        }
        return minv;
    }

    // Inner class representing a node in the binary search tree
    private static class TreeNode implements Serializable {
        Question question;
        TreeNode leftChild;
        TreeNode rightChild;

        public TreeNode(Question question) {
            this.question = question;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	
}
