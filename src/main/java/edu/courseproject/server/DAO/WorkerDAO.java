package edu.courseproject.server.DAO;

import edu.courseproject.server.entity.Worker;

public interface WorkerDAO extends BaseDAO<Worker> {
    Worker findStatusByUserId(Long idUser);
   // boolean create(Worker worker,Long idregion);
    long findIdUserByIdWorker(Long idWorker);
}
