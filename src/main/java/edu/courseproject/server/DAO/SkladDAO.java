package edu.courseproject.server.DAO;

import edu.courseproject.server.entity.Sklad;

public interface SkladDAO extends BaseDAO<Sklad> {
    Sklad findByID(Long idOrder);
}
