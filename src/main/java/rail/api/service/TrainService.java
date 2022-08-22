package rail.api.service;

public interface TrainService {
    void createTrain();

    void updateTrain();

    void removeTrain();

    void findTrainById();

    void getSchedule();

    void getRoute();
}
