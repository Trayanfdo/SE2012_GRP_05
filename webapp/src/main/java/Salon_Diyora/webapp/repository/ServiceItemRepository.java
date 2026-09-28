package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.SalonServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceItemRepository extends JpaRepository<SalonServiceItem, Long> {
}