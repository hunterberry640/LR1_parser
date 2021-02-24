/*
Author: Hunter Berry
Date: Feb 17 2021
Description: java implementation of a LR(1) parser

Data structures:
Queue: holds input
Stack: holds stack items
StackObj: holds state, nonterminal and terminal symbols
*/
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;
class LR1{
    //create queue
    //create stack initialize with (--:0)
    static ArrayList<String> inputQueue = new ArrayList<>();
    static Stack<StackObj> stack= new Stack<>();
    public static void main(String[] args) {
        StackObj initObj = new StackObj(); // push starting stack object
        stack.push(initObj);
        
        //tokenize input then put in queue
        String token;
        StringTokenizer st = new StringTokenizer(args[0], ")(*+-/", true);  
        while (st.hasMoreTokens()) {  
            token = st.nextToken();  
            inputQueue.add(token);
        } 
        inputQueue.add("$");

        //enter main loop
        boolean valid = true;
        while(inputQueue.size()>0 && valid){
            valid = parseTable();
            System.out.println("Stack: " + stack.toString() + "\tQueue: " + inputQueue.toString());
            if(stack.peek().getState() == 1 && stack.peek().getNonterminal().equals("E") && inputQueue.get(0).equals("$")){
                break;
            }
        }
        
        if(valid){ // check if successfully completed
            System.out.println("Valid expression, value = " + stack.pop().getTerminal());
        }else{
            System.out.println("Invalid expression");
        }
    }

    public static boolean parseTable(){
        StackObj curr = stack.peek();
        String input = inputQueue.get(0);
        
        switch(curr.getState()){
            case 0: 
                switch(input){
                    case "(": shift(4); break;
                    default: 
                        if (input.matches("\\d+\\.\\d+")) {
                                shift(5);
                                break;
                            }else if(input.matches("\\d+")){
                                shift(5);
                                break;
                            }else{
                               
                                return false;
                            }
                }
                break;

            case 1: 
                switch(input){
                    case "+": 
                    case "-": shift(6); break;
                    case "$": return true;
                    default: return false;
                } break;

            case 2: 
                switch(input){
                    case "+": 
                    case "-": reduce("E"); break;
                    case "*": 
                    case "/": shift(7); break;
                    case ")": 
                    case "$": reduce("E"); break;
                    default: return false;
                } break;

            case 3: 
                switch(input){
                    case "+": 
                    case "-": reduce("T"); break;
                    case "*": 
                    case "/": reduce("T"); break;
                    case ")": case "$": reduce("T"); break;
                    default: return false;
                }break;

            case 4:
                switch(input){
                    case "(": shift(4); break;
                    default: 
                        if (input.matches("\\d+\\.\\d+")) {
                                shift(5); break;
                            }else if(input.matches("\\d+")){
                                shift(5); break;
                            }else{
                                return false;
                            }
                } break;

            case 5: 
                switch(input){
                    case "+": 
                    case "-": reduce("F");break;
                    case "*": 
                    case "/": reduce("F"); break;
                    case ")": 
                    case "$": reduce("F"); break;
                    default: return false;
                }
                break;
            case 6: 
                switch(input){
                    case "(": shift(4); break;
                    default: 
                        if (input.matches("\\d+\\.\\d+")) {
                                shift(5); break;
                            }else if(input.matches("\\d+")){
                                shift(5); break;
                            }else{
                                return false;
                            }
                } break;

            case 7: 
                switch(input){
                    case "(": shift(4); break;
                    default: 
                        if (input.matches("\\d+\\.\\d+")) {
                                shift(5); break;
                            }else if(input.matches("\\d+")){
                                shift(5); break;
                            }else{
                                return false;
                            }
                } break;

            case 8: 
            switch(input){
                case "+": 
                case "-": shift(6); break;
                case ")": shift(11); break;
                default: return false;
            }break;

            case 9: 
                switch(input){
                    case "+": 
                    case "-": reduceOperation(); break;
                    case "*": 
                    case "/": shift(7); break;
                    case ")": 
                    case "$": reduceOperation(); break;
                    default: return false;
                } break;

            case 10: 
                switch(input){
                    case "+": 
                    case "-": reduceOperation(); break;
                    case "*": 
                    case "/": reduceOperation(); break;
                    case ")": 
                    case "$": reduceOperation(); break;
                    default: return false;
                } break;
            case 11: 
            switch(input){
                case "+": 
                case "-": reduceOperation(); break;
                case "*": 
                case "/": reduceOperation(); break;
                case ")": 
                case "$": reduceOperation(); break;
                default: return false;
            } break;
        }
        return true;
    }


    public static void shift(int state){ // method for pushing from queue to stack
        StackObj obj;
        String input = inputQueue.get(0);
        inputQueue.remove(0);
        switch(input){
            case "+": 
            case "-":
            case ")": 
            case "*": 
            case "/":
            case "(": 
                obj = new StackObj(input, state); 
                stack.push(obj); 
                break;
            default: 
				if (input.matches("\\d+\\.\\d+")) {
                        obj = new StackObj("n", input, state); 
                        stack.push(obj); 
                        break;
					}else if(input.matches("\\d+")){
                        obj = new StackObj("n", input, state); 
                        stack.push(obj); 
                        break;
					}else{
                        break;
					}
                }
        
    }

    public static void reduce(String newNT){
        StackObj curr = stack.pop();
        curr.setNonterminal(newNT);
        curr.setState(nonTermTable(newNT));
        stack.push(curr);
    }

    public static void reduceOperation(){
        StackObj second = stack.pop();
        StackObj op= stack.pop();
        StackObj first = stack.pop();
        
        StackObj result;
        if(second.getTerminal().equals(")")){
            op.setNonterminal("F"); result = op; 
            op.setState(nonTermTable("F"));
            stack.push(op);
            return;
        } 
        
        switch(op.getTerminal()){
            case "+": 
            case "-": result = calc(first, op ,second); break; 
            case "*": 
            case "/": result = calc(first, op ,second); break;
            case ")": 
            default: result = new StackObj(); break;
        }
        stack.push(result);
    }

    public static StackObj calc(StackObj first, StackObj op, StackObj second){
        double firstNum = Double.parseDouble(first.getTerminal());
        double secondNum = Double.parseDouble(second.getTerminal());
        String opVal = op.getTerminal();
        double result=0;
        StackObj returnObj;
        
        switch(opVal){
            case "+":
                result = firstNum+secondNum;
                returnObj= new StackObj("E", String.valueOf(result), first.getState());
                break;
            case "-":
                result = firstNum-secondNum;
                returnObj= new StackObj("E", String.valueOf(result), first.getState());
                break;
            case "*":
                result = firstNum*secondNum;
                returnObj= new StackObj("T", String.valueOf(result), first.getState());
                break;
            case "/":
                result = firstNum/secondNum;
                returnObj= new StackObj("T", String.valueOf(result), first.getState());
                break;
            default:
                returnObj = new StackObj();
        }
         
        return returnObj;
    }
    public static int nonTermTable(String nonTerm){
        int state = stack.peek().getState();
        switch(nonTerm){
            case "E":
                switch(state){
                    case 0: return 1; 
                    case 4: return 8;
                }
            case "T":
                switch(state){
                    case 0: return 2; 
                    case 4: return 2;
                    case 6: return 9; 
                }
            case "F":
                switch(state){
                    case 0: return 3; 
                    case 4: return 3;
                    case 6: return 3;
                    case 7: return 10;  
                }
            default:
                return 0;
            
        }

    }
}

class StackObj{

    private String nonterminal;
    private String terminal;
    private int state; 
    
    StackObj(){ // overloaded constructors to initialize the object
        this.nonterminal = "";
        this.terminal = "";
        this.state = 0;
    }
    StackObj(String terminal, int state){
        this.nonterminal = "";
        this.terminal = terminal;
        this.state = state;
    }
    StackObj(String nonterminal, String terminal, int state){
        this.nonterminal = nonterminal;
        this.terminal = terminal;
        this.state = state;
    }
    StackObj(String nonterminal, int terminal, int state){
        this.nonterminal = nonterminal;
        this.state = state;
    }

    public String getNonterminal() {
        return nonterminal;
    }

    public void setNonterminal(String nonterminal) {
        this.nonterminal = nonterminal;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if(nonterminal != ""){
            return "(" + nonterminal + "=" + terminal+ ":" + state + ")";
        }else{
            return "(" + terminal + ":" + state + ")";
        }
        
    }
}