Connection con =
DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/db",
    "root",
    "password"
);
Task<Void> task = new Task<>() {
    @Override
    protected Void call() {
        // background work
        return null;
    }
};

new Thread(task).start();