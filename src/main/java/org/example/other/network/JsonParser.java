package org.example.other.network;

import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class JsonParser {

    public static String toJson(Map<String, ?> map){
        StringBuilder builder = new StringBuilder("{");
        for(Map.Entry<String, ?> entry : map.entrySet()){
            builder.append("\"").append(entry.getKey()).append("\"").append(": ");
            if(entry.getValue() instanceof Collection<?> collection) builder.append(toJson(collection));
            else if(entry.getValue() == null) builder.append("\"null\"");
            else if(entry.getValue() instanceof Map<?, ?> map1) builder.append(toJson((Map<String, Object>) map1));
            else if(entry.getValue() instanceof Number) builder.append(entry.getValue());
            else builder.append("\"").append(entry.getValue().toString()).append("\"");
            builder.append(", ");
        }
        builder.delete(builder.length()-2, builder.length());
        builder.append("}");
        return builder.toString();
    }

    private static String toJson(Collection<?> collection){
        StringBuilder builder = new StringBuilder("[");
        for(Object o : collection){
            if(o instanceof String s) builder.append("\"").append(s).append("\"");
            else if(o instanceof Map<?, ?> map) builder.append(toJson((Map<String, Object>) map));
            else if(o instanceof Collection<?> coll) builder.append(toJson(coll));
            builder.append(", ");
        }
        builder.delete(builder.length()-2, builder.length());
        builder.append("]");
        return builder.toString();
    }



    public static Map<String, Object> parse(String s) {
        try {
            Map<String, Object> result = new HashMap<>();

            s = s.trim();

            if (s.startsWith("{") && s.endsWith("}")) s = s.substring(1, s.length() - 1);
            else throw new JsonParseException("Incorrect { and }!");

            StringBuilder key = null;
            boolean keyIsParsed = false;
            Object val = null;
            boolean lookingForValue = false;


            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if(Character.isSpaceChar(c) || c < 20) continue;
                if (!keyIsParsed) {
                    if (c == '\"' && key == null) key = new StringBuilder();
                    else if (key != null && c != '\"') key.append(c);
                    else if (c == '\"') keyIsParsed = true;
                } else if (c == ':') {
                    if (lookingForValue) throw new JsonParseException("Too many : !");
                    lookingForValue = true;
                } else if (val != null && c == ',') {
                    result.put(key.toString(), val);
                    keyIsParsed = false;
                    lookingForValue = false;
                    key = null;
                    val = null;
                } else {
                    int end = findConnectedToken(s, i);
                    if (c == '\"') {
                        val = s.substring(i + 1, end);
                        //System.out.println("val1: "+val);
                    } else if (isBracket(c)) {
                        if (c == '[') val = parseArray(s.substring(i, end + 1));
                        else val = parse(s.substring(i, end + 1));
                        //System.out.println("val2: "+val);
                    } else {
                        String numString = s.substring(i, end + 1);
                        if (numString.contains(".")) val = Double.parseDouble(numString);
                        else val = Integer.parseInt(numString);
                        //System.out.println("val3: "+val);
                    }
                    i = end;
                }
            }
            if("null".equals(val)) val = null;
            result.put(key.toString(), val);
            return result;
        } catch (Exception e){
            throw new JsonParseException("Incorrect json!", e);
        }
    }

    public static List<Object> parseArray(String s){
        List<Object> result = new ArrayList<>();

        s = s.trim();
        //log.info(s);

        if (s.startsWith("[") && s.endsWith("]")) s = s.substring(1, s.length() - 1);
        else throw new JsonParseException("Incorrect [ and ]!");

        Object val = null;


        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(Character.isSpaceChar(c) || c < 20) continue;

            if(val != null && c == ','){
                result.add(val);
                val = null;
            }
            else{
                int end = findConnectedToken(s, i);
                if(c == '\"'){
                    val = s.substring(i+1, end);
                } else if(isBracket(c)){
                    if(c == '[') val = parseArray(s.substring(i, end+1));
                    else val = parse(s.substring(i, end+1));

                } else{
                    String numString = s.substring(i, end+1);
                    if(numString.contains(".")) val = Double.parseDouble(numString);
                    else val = Integer.parseInt(numString);
                }
                i=end;
            }
        }
        result.add(val);


        return result;
    }

    private static int findConnectedToken(String s, int start){
        char target = 0;
        char t = s.charAt(start);
        if(t == '{') target = '}';
        else if(t == '[') target = ']';
        else if(t == '\"') target = '\"';

        if(target == 0){
            int pointer = start;
            while (pointer < s.length()){
                char c = s.charAt(pointer);
                //System.out.println("Num char: "+c+" "+(int)c);
                if(!Character.isDigit(c) && c != '-' && c != '.') break;
                pointer++;
            }
            return pointer-1;
        } else if(target == '}' || target == ']'){
            Deque<Character> brackets = new ArrayDeque<>();
            brackets.push(s.charAt(start));
            int pointer = start+1;
            while (!brackets.isEmpty() && pointer < s.length()){
                char c = s.charAt(pointer);
                if(isBracket(c)){
                    if(isOpenBracket(c)) brackets.push(c);
                    else{
                        if(brackets.pop() != reverseBracket(c)) throw new JsonParseException("Incorrect brackets at index "+pointer);
                        if(brackets.isEmpty()) break;
                    }
                }
                pointer++;
            }
            return pointer;
        } else{
           boolean skip = false;
            int pointer = start+1;
           while (pointer < s.length()){
               char c = s.charAt(pointer);
               if(c == '\"' && !skip) return pointer;

               pointer++;
               if(c == '\\' && !skip){
                   skip = true;
                   continue;
               }
               skip = false;
           }
           throw new JsonParseException("No closing \" found!");
        }
    }

    private static boolean isBracket(char c){
        return c == '{' || c =='}' || c == '[' || c == ']';
    }

    private static boolean isOpenBracket(char c){
        return c == '{' || c == '[';
    }

    private static char reverseBracket(char c){
        if(c == '[') return ']';
        if (c == ']') return '[';
        if(c == '{') return '}';
        if(c == '}') return '{';
        return 0;
    }


}
