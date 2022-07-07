import java.util.List;
import java.util.Stack;

public class ValidParenthesesSolution {

    public static void main(String[] args) {
        ValidParenthesesSolution solution = new ValidParenthesesSolution();
        System.out.println(solution.isValid("()"));
        System.out.println(solution.isValid("()[]{}"));
        System.out.println(solution.isValid("(]"));
        System.out.println(solution.isValid("("));
        System.out.println(solution.isValid("]"));
        System.out.println(solution.isValid("([("));
    }

    public boolean isValid(String s) {
        try {
            int stringLength = s.length();
            if (stringLength % 2 != 0) {
                return false;
            }
            Stack<Character> openParentheses = new Stack<>();
            List<Character> openParenthesesTypes = List.of('{', '[', '(');
            List<Character> closedParenthesesTypes = List.of('}', ']', ')');
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);

                int indexOfClosedBracket = closedParenthesesTypes.indexOf(ch);
                if (indexOfClosedBracket != -1) {
                    char openBracketFromStack = openParentheses.pop();
                    int indexOfOpenedBracket = openParenthesesTypes.indexOf(openBracketFromStack);
                    if (indexOfClosedBracket != indexOfOpenedBracket) {
                        return false;
                    }
                }

                if (openParenthesesTypes.contains(ch)) {
                    openParentheses.push(ch);
                }
            }

            return openParentheses.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}
