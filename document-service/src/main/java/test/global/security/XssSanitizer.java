package test.global.security;

import org.springframework.stereotype.Component;

@Component
public class XssSanitizer {
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // <script> 가 &lt;script&gt; 아런 형태로 바뀌면서 실행을 방지.
        return input
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("&", "&amp;");
    }
}
