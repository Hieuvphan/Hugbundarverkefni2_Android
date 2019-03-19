package hi.hugbo.verywowchat.Models;



public class RandomUtils {

    /**
     * Returns a stack trace in the form of a string.
     *
     * Why?
     *
     * This is a tool for debugging purposes, i.e. if you need to figure out where you are
     * in the stack at the point of invocation.
     *
     * Let's say you don't know who is calling a method you're debugging, so throw this sucker
     * in there.
     *
     * @return stack trace
     */
    public static String getStackTrace() {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        String msg = "";
        for (StackTraceElement ste : stes) {
            msg += ste.toString() + "\n";
        }

        return msg;
    }
}
