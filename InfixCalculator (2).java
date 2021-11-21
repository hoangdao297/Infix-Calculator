import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.lang.String;
import java.math.*;

// Nicholas Pakaluk's character and double stack classes
class CharStack{
    // private fields
    private int number, size;
    private char [] data;

    // constructor
    public CharStack (int size) {
        this.number = 0;
        this.size = size;
        this.data = new char[size];
    }

    // push / pop / peek
    public void push (char element) {
        if(this.number == this.size)
            this.expand(1);
        this.data[this.number++] = element;
    }
    public char pop () throws Exception {
        if(this.number-- > 0)
            return this.data[this.number];
        throw new Exception("Stack Empty");
    }
    public char peek () throws Exception {
        if(this.number > 0)
            return this.data[this.number - 1];
        throw new Exception("Stack Empty");
    }

    // height
    public int height () {
        return this.number;
    }

    // print
    public void print () {
        String s = "Stack (bottom->top): ";
        for(int i = 0; i < this.number; i++)
            s += this.data[i] + " ";
        System.out.println(s);
    }

    // private methods
    private void expand (int n) {
        if(n != 0){
            int min = Math.min(this.size, this.size + n);
            char [] t = new char[this.size + n];
            for(int i = 0; i < min; i++)
                t[i] = this.data[i];
            this.data = t;
            this.size += n;
        }
    }
}
class DoubleStack{
    // private fields
    private int number, size;
    private double [] data;

    // constructor
    public DoubleStack (int size) {
        this.number = 0;
        this.size = size;
        this.data = new double[size];
    }

    // push / pop / peek
    public void push (double element) {
        if(this.number == this.size)
            this.expand(1);
        this.data[this.number++] = element;
    }
    public double pop () throws Exception {
        if(this.number-- > 0)
            return this.data[this.number];
        throw new Exception("Stack Empty");
    }
    public double peek () throws Exception {
        if(this.number > 0)
            return this.data[this.number - 1];
        throw new Exception("Stack Empty");
    }

    // height
    public int height () {
        return this.number;
    }

    // print
    public void print () {
        String s = "Stack (bottom->top): ";
        for(int i = 0; i < this.number; i++)
            s += this.data[i] + " ";
        System.out.println(s);
    }

    // private methods
    private void expand (int n) {
        if(n != 0){
            int min = Math.min(this.size, this.size + n);
            double [] t = new double[this.size + n];
            for(int i = 0; i < min; i++)
                t[i] = this.data[i];
            this.data = t;
            this.size += n;
        }
    }
}

// Nicholas Pakaluk's Viper class
class Viper {
    public static String [] tests = {
        "((-8.65 % 6.04 * (7.7 - 4.6)))",  // -8.091
        "sin(((-2.24) * (-4.76 * 2.71)))", // -0.5816617703733259
        "cosh(ln(ceil((3.67))))",          // 2.125
        "round((tan((2.54 / 4.11))))",     // 1
        "(((1.62) & ((-6.24))))",          // Invalid Syntax: Unknown Character
        "(((1.09)) - ((-2.45)))",          // 3.54
        "log2(((5.82) % ((-3.74))))",      // Invalid Syntax: Invalid Function Syntax
    };
    public static String generate (int n, boolean m) {
        /*
        Generate syntactiacally correct expressions
        through the use of a simple context-free
        grammar. The grammar is defined as the
        following:
        G = (V, E, R, S)
        V = {'S', 'o', 'u', 'r'}
        E = {'(', ')', '+', '-', '*', '/', '%',
             '^', 'ln', 'abs', 'cos', 'exp', 'log',
             'sin', 'tan', 'acos', 'asin', 'atan',
             'ceil', 'cosh', 'sinh', 'sqrt', 'tanh',
             'floor', 'round', {-10.00 <= r <= 10.00}}
        R = S -> "(S)"|"SoS"|"u(S)|"|"r",
            o -> '+'|'-'|'*'|'/'|'%'|'^',
            u -> 'ln'|'abs'|'cos'|'exp'|'log'|
                 'sin'|'tan'|'acos'|'asin'|
                 'atan'|'ceil'|'cosh'|'sinh'|
                 'sqrt'|'tanh'|'floor'|'round'
            r -> {-10.00 <= r <= 10.00}
        S = 'S'
        */
        String [] rules = {"(S)", "S o S", "u(S)", "r"};
        String [] u = {"ln",   "abs",  "cos",  "exp",  "log",
                       "sin",  "tan",  "acos", "asin", "atan",
                       "ceil", "cosh", "sinh", "sqrt", "tanh",
                       "floor", "round"};
        String o = "+-*/%^";
        String s = "S";
        int counter = 0;
        while(counter < n){
            for(int i = s.length() - 1; i >= 0; i--){
                if(counter >= n)
                    break;
                if(s.charAt(i) == 'S'){
                    String t = rules[(int) Math.floor((m ? 3 : 2) * Math.random())];
                    s = s.substring(0, i) + t + s.substring(i + 1);
                }
                counter++;
            }
        }
        for(int i = s.length() - 1; i >= 0; i--){
            char c = s.charAt(i);
            switch(c){
                case 'o':
                    char A = o.charAt((int) Math.floor((double) o.length() * Math.random()));
                    s = s.substring(0, i) + A + s.substring(i + 1);
                break;
                case 'u':
                    String B = u[(int) Math.floor(u.length * Math.random())];
                    s = s.substring(0, i) + B + s.substring(i + 1);
                break;
                case 'S':
                    double C = Math.round((m ? 2000 : 1000) * Math.random() - (m ? 1000 : 0)) / 100.0;
                    s = s.substring(0, i) + C + s.substring(i + 1);
                break;
            }
        }
        return s;
    }
    public static char convert (String o) throws Exception {
        /*
        Convert a string representation of a function
        into a char replacement for the operator stack.
        Start by removing leading and following whitespace,
        then compare the result to a plethora of options.
        If an option is selected, return the appropriate
        replacement. Otherwise, throw an exception.
        */
        o = o.trim();
        if(o.length() == 0)
            throw new Exception("No function.");
        switch (o) {
            case "-":     return 'A';
            case "ln":    return 'B';
            case "abs":   return 'C';
            case "cos":   return 'D';
            case "exp":   return 'E';
            case "log":   return 'F';
            case "sin":   return 'G';
            case "tan":   return 'H';
            case "acos":  return 'I';
            case "asin":  return 'J';
            case "atan":  return 'K';
            case "ceil":  return 'L';
            case "cosh":  return 'M';
            case "sinh":  return 'N';
            case "sqrt":  return 'O';
            case "tanh":  return 'P';
            case "floor": return 'Q';
            case "round": return 'R';
        }
        throw new Exception("Unknown Function");
    }
    public static int precedence (char c) {
        /*
        Return the precedence of an operator.
        Precedence follows the regular pattern,
        except that parentheses are given least
        precedence, so that things are processed
        appropriately. Unary operators (which
        includes functions) have the highest
        precedence. Digits and alphanumeric
        characters in the allowed functions have
        0 precedence, while unknown characters
        have negative precedence. This helps the
        main loop of the algorithm ignore and
        dismiss characters correctly.
        */
        if(c >= '0' && c <= '9')
            return 0;
        else if(c >= 'a' && c <= 'x'){
            switch(c){
                case 'j': case 'k':
                case 'm': case 'v':
                return -1;
            }
            return 0;
        }
        else if(c >= 'A' && c <= 'R')
            return 5;
        switch(c){
            case ' ': case '.': return  0;
            case '(': case ')': return  1;
            case '+': case '-': return  2;
            case '*': case '/': case '%': return  3;
            case '^': return  4;
        }
        return -1;
    }
    public static double evaluate (String expression) throws Exception {
        /*
        A double-stack direct infix expression evaluation
        algorithm. I (Nicholas Pakaluk) discovered and
        implemented it myself. It uses a strategy similar
        to the translate-evaluate system, but with one loop
        instead of two. I also added a large amount of extra
        functionality, such as mathematical functions, the
        unary '-' operator, and floating-point numbers.
        */

        // normalize input
        expression = '(' + expression + ')';

        // usefull stuff
        boolean l = true;
        int len = expression.length(), j = 0, i = 0;

        // operator and operand stacks
        CharStack ops = new CharStack(len >> 1);
        DoubleStack nums = new DoubleStack(len >> 1);

        // main loop
        try{
            for(; i < len; i++){

                // c is the current character
                char c = expression.charAt(i);

                // handles the pushing of operands
                if(precedence(c) == 0)
                    continue;
                else if((precedence(c) >= 2 && precedence(c) <= 4) || c == ')'){
                    if(precedence(expression.charAt(i - 1)) == 0){
                        double p = 0.0;
                        boolean d = true;
                        try{
                            p = Double.parseDouble(expression.substring(j, i));
                        }
                        catch(NumberFormatException e){
                            d = false;
                        }
                        if (d) {
                            nums.push(p);
                            l = false;
                        }
                    }
                    j = i + 1;
                }
                else if(c != '(')
                    throw new Exception("Unknown character: " + c);

                // if the last thing pushed was an operator, make the minus unary
                if(c == '-' && l)
                    c = 'A';

                // handle the pushing of operators
                if((ops.height() < 1 || ops.peek() == '(') && c != '(' && c != ')'){
                    ops.push(c);
                    l = true;
                    continue;
                }
                else if(c == '('){
                    try{
                        ops.push(convert(expression.substring(j, i)));
                    }
                    catch(Exception e){
                        if(e.getMessage().compareTo("No function.") != 0)
                            throw e;
                    }
                    ops.push(c);
                    l = true;
                    j = i + 1;
                    continue;
                }
                else if(precedence(ops.peek()) < precedence(c) && c != ')'){
                    ops.push(c);
                    l = true;
                    continue;
                }

                // handle the popping and use of operators
                while(precedence(c) <= precedence(ops.peek()) && ops.height() > 0){
                    if(ops.peek() == '('){
                        ops.pop();
                        break;
                    }
                    else{
                        double t = nums.pop();
                        switch(ops.pop()){
                            case '+' -> nums.push(nums.pop() + t); // X + Y
                            case '-' -> nums.push(nums.pop() - t); // X - Y
                            case '*' -> nums.push(nums.pop() * t); // X * Y
                            case '/' -> nums.push(nums.pop() / t); // X / Y
                            case '%' -> nums.push(nums.pop() % t); // X % Y
                            case '^' -> nums.push(Math.pow(nums.pop(), t)); // X ^ Y
                            case 'A' -> nums.push(-t);             // -X
                            case 'B' -> nums.push(Math.log(t));    // ln(X)
                            case 'C' -> nums.push(Math.abs(t));    // abs(X)
                            case 'D' -> nums.push(Math.cos(t));    // cos(X)
                            case 'E' -> nums.push(Math.exp(t));    // exp(X)
                            case 'F' -> nums.push(Math.log10(t));  // log(X)
                            case 'G' -> nums.push(Math.sin(t));    // sin(X)
                            case 'H' -> nums.push(Math.tan(t));    // tan(X)
                            case 'I' -> nums.push(Math.acos(t));   // acos(X)
                            case 'J' -> nums.push(Math.asin(t));   // asin(X)
                            case 'K' -> nums.push(Math.atan(t));   // atan(X)
                            case 'L' -> nums.push(Math.ceil(t));   // ceil(X)
                            case 'M' -> nums.push(Math.cosh(t));   // cosh(X)
                            case 'N' -> nums.push(Math.sinh(t));   // sinh(X)
                            case 'O' -> nums.push(Math.sqrt(t));   // sqrt(X)
                            case 'P' -> nums.push(Math.tanh(t));   // tanh(X)
                            case 'Q' -> nums.push(Math.floor(t));  // floor(X)
                            case 'R' -> nums.push(Math.round(t));  // round(X)
                        }
                    }
                }

                // if we made it this far, push it
                if(precedence(c) > 1){
                    ops.push(c);
                    l = true;
                }
            }
        }
        catch (Exception e) {
            String tmp = "   ";
            for(int k = 0; k < i; k++)
                tmp += ' ';
            throw new Exception(tmp + "^\nInvalid Syntax: " + e.getMessage());
        }

        // assuming all went according to plan, return the answer
        if(ops.height() == 0 && nums.height() == 1)
            return nums.pop();
        throw new Exception("Invalid Syntax");
    }
    public static void eval_print(String expression){
        try{
            System.out.println(evaluate(expression));
        }
        catch(Exception e){
            String t = "   ";
            int n;
            try{
                n = Integer.parseInt(e.getMessage());
            }
            catch(NumberFormatException nfe){
                n = -1;
            }
            if(n >= 0){
                for(int i = 0; i < n; i++)
                    t = t + ' ';
                System.out.println(t + '^');
                System.out.println("Invalid Expression: Unknown Character");
            }
            else
                System.out.println("Invalid Expression: " + e.getMessage());
        }
    }
}

// Hoang Dao's stack class
class operatorStack{
    private int maxsize;
    private char[] array;
    private int top;
    public operatorStack (int size){
        this.maxsize =size;
        this.array = new char[this.maxsize];
        this.top = -1;
    }
    public boolean isFull(){
        return (this.top==this.maxsize-1);
    }
    public boolean isEmpty(){
        return (this.top==-1);
    }
    public void push(char c){
        if (isFull()) return;
        this.array[++this.top]=c;
    }
    public char pop(){
        if (isEmpty()) return '\0';
        return this.array[this.top--];
    }
    public char peek(){
        if (isEmpty()) return '\0';
        return this.array[this.top];
    }
}

// public class
public class InfixCalculator {
    // Hoang Dao's floating point validation method
    public static boolean validateNum (String str) {
        boolean ans = Pattern.matches(" *\\d+(\\.\\d+)? *", str);
        return ans;
    }
    // Hoang Dao's precedence method
    private static int precedence (char c) {
        switch (c) {
            case '(': case ')': return 1;
            case '^': return 2;
            case '*': case '/': case '%': return 3;
            case '+': case '-': return 4;
            default: return -1;
        }
    }
    // Hoang Dao's Shunting Yard algorithm
    public static String processintopos (String input) throws Exception {
        operatorStack stack = new operatorStack(input.length());
        input = "(" + input + ")";
        int len = input.length();
        int operandnum = 0, operatornum = 0;
        String postfix = "";
        String tempstr = ""; boolean checkstr = false;
        for (int i = 0; i < len; i++) {
            if ((input.charAt(i) >= 48 && input.charAt(i) <= 57) || input.charAt(i) == 46){
                tempstr += input.charAt(i);
                checkstr = true;
            }
            else{
                if (checkstr) {
                    if(!validateNum(tempstr)) throw new Exception("Invalid Syntax");
                    else{
                        postfix += (tempstr + " ");
                        operandnum++;
                        tempstr = ""; checkstr = false;
                    }
                }
                if (precedence(input.charAt(i)) == 1){
                    if (input.charAt(i) == '(') stack.push(input.charAt(i));
                    else{
                        while (stack.peek() != '('){
                            postfix += stack.pop() + " ";
                        }
                        stack.pop();
                    }
                }
                else{
                    operatornum++;
                    while(precedence(stack.peek()) > 1 && precedence(stack.peek()) <= precedence(input.charAt(i))){
                        postfix += stack.pop() + " ";
                    }
                    stack.push(input.charAt(i));
                }
            }
        }
        if (!(operandnum - operatornum == 1)) throw new Exception("Invalid Syntax");
        if (!stack.isEmpty()) throw new Exception("Invalid Syntax");
        return postfix;
    }
    // Surafel Tadesse's postfix expression evaluation algorithm
    public static double EVPostfix (String exp) throws Exception {
		DoubleStack stack = new DoubleStack(exp.length() >> 1);
        int j = 0;
		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);
			if (c == ' ') {
                try {
                    stack.push(Double.parseDouble(exp.substring(j, i)));
                }
                catch (Exception e) {

                }
                j = i + 1;
            }
            else if (Character.isDigit(c) || c == '.') {
                continue;
			}
			else {
                j = i + 1;
                double x = 0.0, y = 0.0;
                try {
                    y = stack.pop();
                    x = stack.pop();
                }
                catch (Exception e) {
                    throw new Exception("Invalid Syntax");
                }
				switch (c) {
					case '+' -> stack.push(x + y);
					case '-' -> stack.push(x - y);
					case '/' -> stack.push(x / y);
					case '*' -> stack.push(x * y);
                    case '^' -> stack.push(Math.pow(x, y));
                    case '%' -> stack.push(x % y);
			    }
			}
		}
        try{
            return stack.pop();
        }
        catch (Exception e) {
            throw new Exception("Invalid Syntax");
        }
	}
    // Cheima Aouati's input correction method
    public static String check (String s) throws Exception {
        CharStack stack = new CharStack(s.length() >> 1);
        String final_str = "";
        Character p;
        boolean paran = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(s.charAt(i));
                final_str += s.charAt(i);
            }
            else if (s.charAt(i) == ')') {
                if (stack.height() == 0)
                    paran = false;
                else{
                    try{
                        p = stack.pop();
                    }
                    catch (Exception e) {
                        throw new Exception("Parentheses Not Matching");
                    }
                }
                final_str += s.charAt(i);
            }
            else if((precedence(s.charAt(i)) == -1)){
                if (s.charAt(i) == ' ')
                    continue;
                else if ((s.charAt(i) >= '0' && s.charAt(i) <= '9') || s.charAt(i) == '.')
                    final_str += s.charAt(i);
                else
                    throw new Exception("Unknown Character: " + s.charAt(i));
            }
            else
                final_str += s.charAt(i);
        }
        if (stack.height() > 0)
            paran = false;
        if (!paran)
            throw new Exception("Parentheses Not Matching");
        return final_str;
    }
    // Nicholas Pakaluk's main method
    public static void main (String [] args) {
        String [] tests = {
            "(4 + 8) * (6 - 5) / ((3 - 2) * (2 + 2))",  // 3
            "(300 + 20) * (43 - 23) / (6 + 2)",         // 800
            "8 + 3 * 4 - 2 ^ 5 % 6 / (9 - 7)",          // 19
            "(3.2 + 2.2) * (7.5 / 2.5)",                // 16.2
            "12 * 3 +",                                 // Error
            "(3 - 2) + (5 - 4)",                        // 2
            "(3..2 - 2) + (5 - 4)",                     //Error
            "(3 - 2) + - (5 - 4)",                      //Error
            "(.2 - 2) + (5 - 4)",                       //Error
            "(3. - 2) + (5 - 4)",                       //Error
            "(3 - 2 + (5 - 4)",                         //Error
            "3 - 2) + (5 - 4)",                         //Error
            "(3 - 2) + (5 - )",                         //Error
            "(3 - 2)(5 - 4)",                           //Error
            "3*6%3-8/4",                                //-2
            "8+3*4-2^5%6/(9-7)",                        // 19
            "3*6=3-8%4",
            "3*6^3-8%4",
            "3*6?3-8%4",
            "3*6>3-8%4"
        };
        int method = 1;
        Scanner k = new Scanner(System.in);
        String input = "";
        System.out.println("\nWelcome to our infix calculator!\n\n" +
                             "This is an advanced command line tool for evaluating\n" +
                             "infix expressions, with built-in support for mathematical\n" +
                             "functions such as sin(x), sqrt(x), etc. Here is a list of\n" +
                             "available commands:\n\n" +
                             "method 1 - This command switches the program to use the\n" +
                             "           team's algorithms, which fulfill the requirements\n" +
                             "           of the assignment.\n\n" +
                             "method 2 - This command switches the program to use\n" +
                             "           Nick's algorithm, which can evaluate mathematical\n" +
                             "           functions and the unary minus operator.\n\n" +
                             "run - This command runs a series of tests using\n" +
                             "      the currently selected method.\n\n" +
                             "gen - This command generates a syntctically correct\n" +
                             "      expression using a simple context free grammar.\n" +
                             "      It adds mathematical functions when the currently\n" +
                             "      selected method is method 2.\n\n" +
                             "quit - This command immediately quits the application.\n");
        while(input.compareTo("quit") != 0){
            System.out.print(">>> ");
            input = k.nextLine();
            switch(input){
                case "quit":
                break;
                case "method 1":
                    method = 1;
                    System.out.println("Method 1 is now in use: The Team's algorithms");
                break;
                case "method 2":
                    method = 2;
                    System.out.println("Method 2 is now in use: Nick's algorithm");
                break;
                case "run":
                    int j = (method == 1 ? tests.length : Viper.tests.length);
                    for(int i = 0; i < j; i++){
                        System.out.println(">>> " + (method == 1 ? tests[i] : Viper.tests[i]));
                        try{
                            if(method == 1){
                                System.out.println(EVPostfix(processintopos(check(tests[i]))));
                            }
                            else{
                                System.out.println(Viper.evaluate(Viper.tests[i]));
                            }
                        }
                        catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                break;
                case "gen":
                    input = Viper.generate(25, method == 2);
                    System.out.println(">>> " + input);
                default:
                    try{
                        if(method == 1){
                            System.out.println(EVPostfix(processintopos(check(input))));
                        }
                        else{
                            System.out.println(Viper.evaluate(input));
                        }
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
            }
        }
        System.out.println("Thank you for choosing us over Python :)");
    }
}
