package unit7.patterns;

/**
 * Thread-safe initialization-on-demand holder Singleton.
 * Prefer dependency injection for most application services.
 */
public final class SingletonConfig {
    private SingletonConfig() {}

    private static class Holder {
        private static final SingletonConfig INSTANCE = new SingletonConfig();
    }

    public static SingletonConfig getInstance() {
        return Holder.INSTANCE;
    }

    public String getCurrency() {
        return "NPR";
    }

    public static void main(String[] args) {
        SingletonConfig a = SingletonConfig.getInstance();
        SingletonConfig b = SingletonConfig.getInstance();
        System.out.println(a == b);
        System.out.println("Currency: " + a.getCurrency());
    }
}
