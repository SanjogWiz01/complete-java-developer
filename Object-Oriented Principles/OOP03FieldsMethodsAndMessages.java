public class OOP03FieldsMethodsAndMessages {
    /*
     * Fields store object data. Methods expose behavior. Calling a method is
     * often described as sending a message to an object.
     */
    static class Thermostat {
        private int targetTemperature;

        Thermostat(int targetTemperature) {
            this.targetTemperature = targetTemperature;
        }

        void increase() {
            targetTemperature++;
        }

        void decrease() {
            targetTemperature--;
        }

        int currentTarget() {
            return targetTemperature;
        }
    }

    public static void main(String[] args) {
        Thermostat thermostat = new Thermostat(22);
        thermostat.increase();
        thermostat.increase();
        thermostat.decrease();

        System.out.println("Target temperature: " + thermostat.currentTarget());
    }
}
