package edu.courseproject.server.DAO;

import java.util.Map;

public interface SkladProductDAO extends BaseDAO {
    Map<Long, Integer> findSuppliersByIdSklad(Long idSkald);
    boolean removeFromSklad(Map<Long,Integer> map, Long idSklad);
    boolean fillSklad(Map<Long,Integer> map);
}
