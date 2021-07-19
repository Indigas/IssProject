package sk.durovic.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import sk.durovic.controller.CarController;
import sk.durovic.model.Car;
import sk.durovic.model.UserDetailImpl;

public interface CarOwnerHelper {
    static boolean isOwnerOfCar(UserDetails userDetail, Car car1) {
        if (userDetail == null || car1 == null || !((UserDetailImpl) userDetail).getCompany().getId().equals(car1.getCompany().getId())) {
            //log.debug("User not authorized to change car.");
            //log.debug("user::" + userDetail);
            return false;
        }

        return true;
    }
}
