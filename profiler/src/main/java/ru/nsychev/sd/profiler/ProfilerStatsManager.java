package ru.nsychev.sd.profiler;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProfilerStatsManager {
    public static final ProfilerStatsManager INSTANCE = new ProfilerStatsManager();

    private final Node root = new Node(null);
    private Node current = root;

    public void enterMethod(String methodName) {
        current = current.enterMethod(methodName);
    }

    public void exitMethod(Long elapsed) {
        current.increment(elapsed);
        current = current.getParent();
    }

    public static ProfilerStatsManager getInstance() {
        return INSTANCE;
    }

    public void printStatistics() {
        System.err.println("================================================================================");
        System.err.println("PROFILER STATISTICS:");
        System.err.printf("%-60s %8s %10s\n", "Method", "Calls", "Total");
        root.printStatistics(0);
    }

    private static class Node {
        private final Node parent;

        private Integer callCount;
        private Long callsElapsed;
        private Map<String, Node> children;

        public Node(Node parent){
            this.parent = parent;
            this.callCount = 0;
            this.callsElapsed = 0L;
            this.children = new LinkedHashMap<>();
        }

        public void increment(Long elapsed) {
            this.callCount++;
            this.callsElapsed += elapsed;
        }

        public Node enterMethod(String methodName) {
            return children.computeIfAbsent(methodName, (String) -> new Node(this));
        }

        Node getParent() {
            return parent;
        }

        public void printStatistics(int offset) {
            StringBuilder prefixBuilder = new StringBuilder();
            for (int i = 0; i < offset - 1; i++) {
                prefixBuilder.append("  ");
            }
            if (offset > 0) {
                prefixBuilder.append("- ");
            }
            String prefix = prefixBuilder.toString();
            int maxLength = 60 - 2 * offset;

            for (Map.Entry<String, Node> item : children.entrySet()) {
                String methodName = item.getKey();
                Node methodInfo = item.getValue();

                if (methodName.length() > maxLength) {
                    methodName = methodName.replaceAll(
                            "\\b([a-z])[a-zA-Z_]+\\.",
                            "$1."
                    );
                }
                if (methodName.length() > maxLength) {
                    methodName = methodName.substring(maxLength - 3) + "...";
                }

                System.err.printf(
                        "%s%-" + maxLength + "s %8d %10d\n",
                        prefix,
                        methodName,
                        methodInfo.callCount,
                        methodInfo.callsElapsed
                );

                methodInfo.printStatistics(offset + 1);
            }
        }
    }
}
