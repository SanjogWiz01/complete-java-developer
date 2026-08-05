package server;

public class InterfaceSegregationService {

    public interface Workable {
        void work();
    }

    public interface Eatable {
        void eat();
    }

    public interface Restable {
        void rest();
    }

    public static class HumanWorker implements Workable, Eatable, Restable {
        @Override
        public void work() {
            System.out.println("Human worker is working on tasks.");
        }

        @Override
        public void eat() {
            System.out.println("Human worker is taking a lunch break.");
        }

        @Override
        public void rest() {
            System.out.println("Human worker is resting.");
        }
    }

    public static class RobotWorker implements Workable {
        @Override
        public void work() {
            System.out.println("Robot worker is working continuously.");
        }
    }

    public static class WorkScheduler {
        public void manageWork(Workable worker) {
            worker.work();
            if (worker instanceof Eatable) {
                ((Eatable) worker).eat();
            }
            if (worker instanceof Restable) {
                ((Restable) worker).rest();
            }
        }
    }

    public static void main(String[] args) {
        WorkScheduler scheduler = new WorkScheduler();
        scheduler.manageWork(new HumanWorker());
        scheduler.manageWork(new RobotWorker());
    }
}
