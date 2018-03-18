package com.leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Trading OMS Systems
 * <p>
 * Created by Sridhar S Shenoy on 3/17/18 at 10:31 PM
 */
public class LongestSubstring {

    @Test
    public void findSubstringInSingleChar() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("aa"), is(1));
    }

    @Test
    public void findSubstringInSingleCharOnAsingleCharInput() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("c"), is(1));
    }


    @Test
    public void findSubstringInDoubleChar() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("ab"), is(2));
    }

    @Test
    public void findSubstringInThreeChar() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("ababcab"), is(3));
    }

    @Test
    public void acceptanceTest1() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("pwwkew"), is(3));
    }

    @Test
    public void acceptanceTest2() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("abcabcbb"), is(3));
    }

    @Test
    public void acceptanceTest3() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("abba"), is(2));
    }

    @Test
    public void acceptanceTest4() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring(""), is(0));
    }

    @Test
    public void acceptanceTest5() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("aaaaabcdeeeeee"), is(5));
    }

    @Test
    public void acceptanceTest6() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("abcdedcba"), is(5));
    }

    @Test
    public void acceptanceTest7() {
        assertThat(new LongestSubstringMain().lengthOfLongestSubstring("tmmzuxt"), is(5));
    }

    private class LongestSubstringMain {

        public int lengthOfLongestSubstring(String input) {
            Map<Character, Integer> charIndexMap = new HashMap<>();
            Set<Integer> maxList = new HashSet<>();
            int lastIndex = 0;
            int currentIndex = 0;
            int max = 0;
            for (Character c : input.toCharArray()) {
                if (charIndexMap.containsKey(c)) {
                    maxList.add(max);
                    int currentlastIndex = charIndexMap.get(c) + 1;
                    if (lastIndex < currentlastIndex)
                        lastIndex = currentlastIndex;
                }
                charIndexMap.put(c, currentIndex++);
                int currentMax = currentIndex - lastIndex;
                if (max < currentMax) {
                    max = currentMax;
                }
            }
            return max;
        }

    }
}
