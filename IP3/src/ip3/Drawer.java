/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip3;

import HomeTutor.drawerTutorController;
import UserQNA.UserQNAController;
import UserQNA.drawerController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author erino
 */
public class Drawer {
    public void drawerPullout(JFXDrawer drawer, User currentUser, JFXHamburger hamburger){
     try {
               
                if(currentUser.getTitleId()==1){

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserQNA/drawer.fxml"));
                VBox box = loader.load();
                drawer.setSidePane(box);
                drawerController controller = loader.getController();

                controller.setData(currentUser);
                }
                else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeTutor/drawerTutor.fxml"));
                VBox box = loader.load();
                drawer.setSidePane(box);
                drawerTutorController controller = loader.getController();

                controller.setData(currentUser);
                }
                HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
                transition.setRate(-1);
                hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    transition.setRate(transition.getRate() * -1);
                    transition.play();

                    if (drawer.isOpened()) {
                        drawer.close();
                        drawer.setDisable(true);
                   
                    } else {
                        drawer.open();
                        drawer.setDisable(false);

                    }
                }
                );
            } catch (IOException ex) {
                Logger.getLogger(UserQNAController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
