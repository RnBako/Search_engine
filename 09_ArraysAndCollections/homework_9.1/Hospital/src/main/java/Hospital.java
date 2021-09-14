public class Hospital {

    public static float[] generatePatientsTemperatures(int patientsCount) {

        //TODO: напишите метод генерации массива температур пациентов
        float[] temperatureData = new float[patientsCount];
        for (int i = 0; i < temperatureData.length; i++) {
            temperatureData[i] =  (float) (Math.ceil(Math.random() * 10) / 10) * (40 - 32) + 32;
        }
        return temperatureData;
    }

    public static String getReport(float[] temperatureData) {
        /*
        TODO: Напишите код, который выводит среднюю температуру по больнице,количество здоровых пациентов,
            а также температуры всех пациентов.
        */
        String patientsTemperatures = "";
        float sumTemperature = 0;
        int healthyCount = 0;
        for (int i = 0; i < temperatureData.length; i++) {
            if (i != temperatureData.length-1) {
                patientsTemperatures = patientsTemperatures + temperatureData[i] + " ";
            } else {
                patientsTemperatures = patientsTemperatures + temperatureData[i];
            }
            sumTemperature += temperatureData[i];
            if (temperatureData[i] >= 36.2F && temperatureData[i] <= 36.9F) { healthyCount++; }
        }
        float averageTemperature = (float) (int) (sumTemperature / temperatureData.length * 100) / 100;

        String report =
                "Температуры пациентов: " + patientsTemperatures +
                        "\nСредняя температура: " + averageTemperature +
                        "\nКоличество здоровых: " + healthyCount;

        return report;
    }
}
