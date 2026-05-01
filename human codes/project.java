class project {
    public static void main(String[] args) {
        System.out.println("Welcome to project.java");
        System.out.println("This is a basic Java project file in /human codes.");

        int a = 10;
        int b = 20;
        int sum = add(a, b);

        System.out.println("Sum of " + a + " and " + b + " = " + sum);
    }

    public static int add(int x, int y) {
        return x + y;
    }
}
