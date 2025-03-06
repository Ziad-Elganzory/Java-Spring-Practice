package com.codezury.springcoredemo.rest;

import com.codezury.springcoredemo.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    //define a private field for dependency
    //@Autowired
    private Coach myCoach;

    //define a constructor for dependency injection
    @Autowired
    public DemoController(
            @Qualifier("cricketCoach") Coach theCoach){
        System.out.println("In constructor: "+ getClass().getSimpleName());
        myCoach = theCoach;
    }
    /*
    Use Setter injection

    @Autowired
    public void setCoach(Coach theCoach) {
        myCoach = theCoach;
    }
    */

    @GetMapping("/dailyworkout")
    public String getDailyWorkout(){
        return myCoach.getDailyWorkout();
    }

    /*
    Check the bean scope
    SINGLETON : True
    PROTOTYPE : False

    @GetMapping("/check")
    public String check() {
        return "Comparing Beans: myCoach == anotherCoach, " + (myCoach == anotherCoach);
    }
     */
}
