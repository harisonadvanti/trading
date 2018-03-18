package com.school;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Trading OMS Systems
 * <p>
 * Created by Sridhar S Shenoy on 3/10/18 at 10:19 AM
 */
public class Number24 {

    @FunctionalInterface
    interface Find<T> {
        T getValueAtIndex(int i);
    }

    public static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/");


    public void execute() {
        int sum = 56;
        int[] input
                = {2, 1, 1, 3, 4, 5};
        for (String s : new HashSet<>(new Sum24NumberList().find(sum, input))) {
            System.out.println(s + "=" + sum);
        }

    }

    @Test
    public void findSimpleExpression() {
        Sum24NumberList nlist = new Sum24NumberList();
        List<String> result = nlist.find(2, new int[]{1, 1});
        assertThat(result, is(Arrays.asList("1+1", "1+1")));
    }

    @Test
    public void singleOperandOperationEvaluatindToZero() {
        Sum24NumberList nlist = new Sum24NumberList();
        List<String> result = nlist.find(0, new int[]{1, 1});
        assertThat(Arrays.asList("1-1","1-1"), is(result));
    }

    @Test
    public void complexExpression() {
        Sum24NumberList nlist = new Sum24NumberList();
        List<String> result = nlist.find(2, new int[]{1, 1, 2});
        assertThat(result, is(Arrays.asList("1-1+2", "1*1*2", "1/1*2", "1+2-1", "1*2*1", "1*2/1", "1-1+2", "1*1*2", "1/1*2", "1+2-1", "1*2*1", "1*2/1", "2+1-1", "2-1+1", "2*1*1", "2*1/1", "2/1*1", "2/1/1", "2+1-1", "2-1+1", "2*1*1", "2*1/1", "2/1*1", "2/1/1")));
    }

    @Test
    public void tokenizeSimpleExpresion() {
        assertThat(new Sum24NumberList().tokenizer("123+45"), is(Arrays.asList("123", "+", "45")));
    }

    @Test
    public void tokenizeComplexExpresion() {
        assertThat(new Sum24NumberList().tokenizer("123+45-67/82"), is(Arrays.asList("123", "+", "45", "-", "67", "/", "82")));
    }

    @Test
    public void singleOperandPostFix() {
        assertThat(new Sum24NumberList().toPostFix(Arrays.asList("2", "+", "3")), is(Arrays.asList("2", "3", "+")));
    }

    @Test
    public void doubleOperandPostFix() {
        assertThat(new Sum24NumberList().toPostFix(Arrays.asList("2", "+", "3", "*", "4")), is(Arrays.asList("2", "3", "4", "*", "+")));
    }

    @Test
    public void doubleOperandPostFixWithHigherPrecedenceFirst() {
        assertThat(new Sum24NumberList().toPostFix(Arrays.asList("2", "*", "3", "+", "4")), is(Arrays.asList("2", "3", "*", "4", "+")));
    }

    @Test
    public void complexPostFix() {
        assertThat(new Sum24NumberList().toPostFix(Arrays.asList("2", "*", "3", "+", "4", "/", "6")), is(Arrays.asList("2", "3", "*", "4", "6", "/", "+")));
    }

    @Test
    public void evaluateSimpleExpression() {
        assertThat(new Sum24NumberList().evaluate("1+1"), is(2));
    }

    @Test
    public void evaluateComplexExpression() {
        assertThat(new Sum24NumberList().evaluate("5+5*2-3"), is(12));
    }

    @Test
    public void getSimpleCombination() {
        assertThat(new Sum24NumberList().getCombination(new int[]{1, 1}), is(Arrays.asList("1+1", "1-1", "1*1", "1/1")));
    }


    @Test
    public void getOperatorCombinationList() {
        List<List<String>> value = Arrays.asList(
                Arrays.asList("+"),
                Arrays.asList("-"),
                Arrays.asList("*"),
                Arrays.asList("/"));
        assertThat(new Sum24NumberList().getOperatorCombinationList(1), is(value));
    }

    @Test
    public void getOperatorCombinationListForTwoOperands() {
        List<List<String>> value = Arrays.asList(
                Arrays.asList("+", "+"),
                Arrays.asList("+", "-"),
                Arrays.asList("+", "*"),
                Arrays.asList("+", "/"),

                Arrays.asList("-", "+"),
                Arrays.asList("-", "-"),
                Arrays.asList("-", "*"),
                Arrays.asList("-", "/"),

                Arrays.asList("*", "+"),
                Arrays.asList("*", "-"),
                Arrays.asList("*", "*"),
                Arrays.asList("*", "/"),

                Arrays.asList("/", "+"),
                Arrays.asList("/", "-"),
                Arrays.asList("/", "*"),
                Arrays.asList("/", "/")

        );
        assertThat(new Sum24NumberList().getOperatorCombinationList(2), is(value));
    }


    private class Sum24NumberList {


        private Map<String, Integer> precedence = new HashMap<>();

        Map<Integer, String> operatorMap = new HashMap<Integer, String>() {
            {
                put(0, "+");
                put(1, "-");
                put(2, "*");
                put(3, "/");
            }
        };

        public Sum24NumberList() {
            precedence.put("+", 1);
            precedence.put("-", 1);
            precedence.put("*", 10);
            precedence.put("/", 10);

        }

        public List<String> find(int sum, int[] input) {


            List<String> expressionForInputSequence = new ArrayList<>();
            List<Integer[]> inputCombi = getAllCombinationRec(input);
            for (Integer[] arr : inputCombi) {

                int[] inp = new int[arr.length];
                int c = 0;
                for (Integer integer : arr) {
                    inp[c++] = integer;
                }

                int[] finp = new int[arr.length];
                int i = 0;
                for (Integer integer : arr) {
                    finp[i++] = integer;
                }
                expressionForInputSequence.addAll(getExpressionForInputSequence(sum, finp));
            }


            return expressionForInputSequence;
        }


        List<List<Integer>> allCombinationOfInput = new ArrayList<>();

        private List<Integer[]> getAllCombinationRec(int[] input) {
            List<Integer> inp = Arrays.stream(input).boxed().collect(Collectors.toList());
            recursive(new ArrayList<>(inp), new ArrayList<>());

            List<Integer[]> result = new ArrayList<>();
            for (List<Integer> finalR : allCombinationOfInput) {
                result.add(finalR.toArray(new Integer[finalR.size()]));
            }
            return result;
        }

        private void recursive(List<Integer> clone, List<Integer> accum) {
            if (clone.size() == 0) {
                allCombinationOfInput.add(accum);

            }

            for (Integer d : clone) {
                ArrayList<Integer> cl = new ArrayList<>(clone);
                cl.remove(d);
                ArrayList<Integer> ac = new ArrayList<>();
                ac.addAll(accum);
                ac.add(d);
                recursive(cl, ac);
            }

        }


        private void recursiveCall(List<List<Integer>> cl) {
            for (List<Integer> allInputComis : cl) {
                for (Integer allInputComi : allInputComis) {

                }
            }
        }

        private List<String> getExpressionForInputSequence(int sum, int[] input) {
            List<String> result = new ArrayList<>();
            List<String> combination = getCombination(input);
            for (String exp : combination) {
                if (sum == evaluate(exp)) {
                    result.add(exp);
                }
            }
            return result;
        }

        private List<String> getCombination(int[] input) {
            List<String> combinations = new ArrayList<>();
            List<List<String>> operatorList = getOperatorCombinationList(input.length - 1);
            for (List<String> operators : operatorList) {
                StringBuilder builder = new StringBuilder();
                int index = 0;
                for (int operand : input) {
                    builder.append(operand);
                    if (index < operators.size())
                        builder.append(operators.get(index++));
                }
                combinations.add(builder.toString());
            }

            return combinations;
        }

        private List<List<String>> getOperatorCombinationList(int length) {
            return getcombiBase4(length, 4, i -> operatorMap.get(i));
        }

        private List<List<String>> getcombiBase4(int n, int a, Find<String> find) {
            int rows = (int) Math.pow(a, n);

            List<List<String>> operatorCombiList = new ArrayList<>();
            for (int i = 0; i < rows; i++) {
                List<String> combi = new ArrayList<>();
                for (int j = n - 1; j >= 0; j--) {
                    combi.add(find.getValueAtIndex((i / (int) Math.pow(a, j)) % a));

                }
                operatorCombiList.add(combi);
            }
            return operatorCombiList;
        }


        private int evaluate(String s) {
            List<String> tokenized = tokenizer(s);
            List<String> postFix = toPostFix(tokenized);

            Deque<Double> stack = new ArrayDeque<>();
            for (String pf : postFix) {
                if (Character.isDigit(pf.charAt(0))) {
                    stack.push(Double.parseDouble(pf));
                } else {
                    double operand2 = stack.pop();
                    double operand1 = stack.pop();
                    switch (pf.charAt(0)) {
                        case '+': {
                            stack.push(operand1 + operand2);
                            break;
                        }
                        case '-': {
                            stack.push(operand1 - operand2);
                            break;
                        }
                        case '/': {
                            stack.push(operand1 / operand2);
                            break;
                        }
                        case '*': {
                            stack.push(operand1 * operand2);
                            break;
                        }
                    }
                }


            }
            double result = stack.pop();
            return (int) result;
        }

        private List<String> toPostFix(List<String> tokenized) {
            Deque<String> stack = new ArrayDeque<String>();
            List<String> postFix = new ArrayList<String>();
            for (String token : tokenized) {
                if (Character.isDigit(token.charAt(0))) {
                    postFix.add(token);
                } else {
                    if (stack.isEmpty()) {
                        stack.push(token);
                        continue;
                    }

                    if (precedence.get(token) > precedence.get(stack.peek())) {
                        stack.push(token);
                    } else {
                        while ((!stack.isEmpty()) && precedence.get(token) <= precedence.get(stack.peek())) {
                            postFix.add(stack.pop());
                        }
                        stack.push(token);
                    }

                }

            }
            while (!stack.isEmpty()) {
                postFix.add(stack.pop());
            }

            return postFix;
        }

        private List<String> tokenizer(String s) {
            List<String> tokenize = new ArrayList<String>();
            String token = "";
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    token += c;

                } else {
                    tokenize.add(token);
                    tokenize.add("" + c);
                    token = "";
                }
            }
            tokenize.add(token);

            return tokenize;
        }
    }
}
