package com.pedro.calculomental.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pedro.calculomental.dao.MongoConnectorDao;
import com.pedro.calculomental.dtos.ActivityDto;
import com.pedro.calculomental.dtos.ActivityDto.ActivityType;
import com.pedro.calculomental.dtos.ActivityListDto;
import com.pedro.calculomental.dtos.UserDto;
import com.pedro.calculomental.dtos.UserSignUpDto;


/**
 * The Class CalculoMentalController.
 */
@Controller
public class CalculoMentalController {

    /**
     * The logger.
     */
    private static Logger LOGGER = LoggerFactory.getLogger(CalculoMentalController.class);

    /**
     * The user dao.
     */
    @Autowired
    private MongoConnectorDao<UserDto> userDao;

    /**
     * The activity dao.
     */
    @Autowired
    private MongoConnectorDao<ActivityDto> activitiesDao;

    /**
     * Show sign up form.
     *
     * @return sign up form model and view
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView showSignUpForm() {
        LOGGER.info("CalculoMentalController.showSignUpForm");
        return new ModelAndView("signup", "userSignUpDto", new UserSignUpDto());
    }


    /**
     * Submit signup form.
     *
     * @param user   UserSignUpDto object containing new user data.
     * @param result the result
     * @param model  the model
     * @return success or error sign up view
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String submitSignupForm(@ModelAttribute("userSignUpDto") UserSignUpDto user, BindingResult result, ModelMap model) {
        LOGGER.info("CalculoMentalController.submitSignupForm.in");

        String error = this.validateSignupForm(user);
        if (error != null)
            return String.format("redirect:signup?error=%s", error);

        LOGGER.info("CalculoMentalController.submitSignupForm - creating user ...");
        user.setRole(UserDto.UserRoles.ALUMNO);
        userDao.create(user);

        LOGGER.info("CalculoMentalController.submitSignupForm.out");
        return "redirect:signup?success";
    }


    /**
     * Validate signup form.
     *
     * @param user userSignUpDto object containing user data from sign up form
     * @return String with error message
     */
    private String validateSignupForm(UserSignUpDto user) {
        LOGGER.info("CalculoMentalController.validateSignupForm.in");

        if (user.getId().isEmpty() || user.getPassword().isEmpty()) {
            LOGGER.info("CalculoMentalController.validateSignupForm.out - Incomplete form");
            return "Por favor, complete todos los campos.";
        }

        if (!user.getPassword().equals(user.getPasswordConfirmed())) {
            LOGGER.info("CalculoMentalController.validateSignupForm.out - Passwords do not match");
            return "Las contraseñas indicadas no coinciden.";
        }

        if (this.userDao.read("id", user.getId()).size() > 0) {
            LOGGER.info("CalculoMentalController.validateSignupForm.out - User already exists");
            return "El usuario ya existe.";
        }

        LOGGER.info("CalculoMentalController.validateSignupForm.out");
        return null;
    }


    /**
     * List activities.
     *
     * @param model the model.
     * @param authentication the authentication.
     * @return the model and view.
     */
    @RequestMapping(value = "/activitylist", method = RequestMethod.GET)
    public ModelAndView listActivities(Model model, Authentication authentication, @RequestParam("type") String operation) {
        LOGGER.info("CalculoMentalController.listActivities.in - operation: {}", operation);

        UserDto user = this.userDao.read("id", authentication.getName()).get(0);

        ActivityListDto activityList = new ActivityListDto();
        ActivityType activityType = ActivityType.getByOperation(operation);

        
        LOGGER.info("CalculoMentalController.listActivities ... getting all activities");
        List<ActivityDto> all_activities = this.activitiesDao.read();
        if (all_activities != null && !all_activities.isEmpty() ) {
            activityList.setAvailableActivities(all_activities.stream()
                    .filter(activity -> !user.getCompletedActivityIds().contains(activity.getId()) && activity.getType().equals(activityType))
                    .collect(Collectors.toList()));
           
            if (user.getCompletedActivityIds() != null && !user.getCompletedActivityIds().isEmpty())
                activityList.setCompletedActivities(all_activities.stream()
                        .filter(activity -> user.getCompletedActivityIds().contains(activity.getId()) && activity.getType().equals(activityType))
                        .collect(Collectors.toList()));
        }

        LOGGER.info("CalculoMentalController.listActivities.out");
        return new ModelAndView("activitylist", "activities", activityList);
    }

    /**
     * Get activity by ID
     *
     * @param model the model.
     * @param authentication the authentication.
     * @return the model and view. (questions, activity id, activity number, activity type
     */
    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public ModelAndView showActivity(Model model, Authentication authentication, @RequestParam("id") String activityId) {
        LOGGER.info("CalculoMentalController.showActivity.in - id: {}", activityId);
        
        List<ActivityDto> all_activities = (activityId != null && !activityId.trim().isEmpty())? 
        		this.activitiesDao.read("id", activityId): null;
        
        List<String> questions = new ArrayList<>();
        ActivityDto activity = null;

        if (all_activities != null && !all_activities.isEmpty()) {
            activity = all_activities.get(0);
            questions = activity.getQuestions();
        }

        //Trim and randomize questions
        Collections.shuffle(questions);
        questions = questions.subList(0, 10);

        ModelAndView modelAndView = new ModelAndView("activity");
        modelAndView.addObject("questions", questions);
        modelAndView.addObject("type", activity.getType().getOperation().toUpperCase());
        modelAndView.addObject("activityNumber", activity.getActivityNumber());
        modelAndView.addObject("activityId", activity.getId());

        LOGGER.info("CalculoMentalController.showActivity ... getting questions - questions: {}", questions);
        LOGGER.info("CalculoMentalController.showActivity ... getting questions - activityId: {}", activity.getId());
        LOGGER.info("CalculoMentalController.showActivity ... getting type - type: {}", activity.getType().getOperation());
        LOGGER.info("CalculoMentalController.showActivity.out");

        return modelAndView;
    }
    
    
    /**
     * Complete current activity.
     *
     * @param model the model.
     * @param authentication the authentication.
     * @param activityId the activity id.
     * @return the model and view.
     */
    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    public ModelAndView completeActivity(@RequestParam("id") String activityId, @RequestParam("type") String operation,
    								Authentication authentication){
        LOGGER.info("CalculoMentalController.completeActity.in");
        
        ModelAndView model = new ModelAndView("activityList");
        
        LOGGER.info("CalculoMentaController.completeActivity - {}", activityId);
        if (activityId!= null && !activityId.trim().isEmpty()){
        	UserDto user = this.userDao.read("id", authentication.getName()).get(0);
        	
        	if (user.getCompletedActivityIds() == null || user.getCompletedActivityIds().isEmpty())
        		user.setCompletedActivityIds(new ArrayList<>());
        	
        	user.getCompletedActivityIds().add(activityId);
        	Long n_updated = this.userDao.update(user);
        	LOGGER.info("CalculoMentalController.completeActivity - Updated user: {}", n_updated);
 
            ActivityListDto activityList = new ActivityListDto();
            ActivityType activityType = ActivityType.getByOperation(operation);
            
            LOGGER.info("CalculoMentalController.completeActivity ... getting all activities");
            List<ActivityDto> all_activities = this.activitiesDao.read();
            if (all_activities != null && !all_activities.isEmpty()) {
                activityList.setAvailableActivities(all_activities.stream()
                        .filter(activity -> !user.getCompletedActivityIds().contains(activity.getId()) && activity.getType().equals(activityType))
                        .collect(Collectors.toList()));

                if (user.getCompletedActivityIds() != null && !user.getCompletedActivityIds().isEmpty())
                    activityList.setCompletedActivities(all_activities.stream()
                            .filter(activity -> user.getCompletedActivityIds().contains(activity.getId()) && activity.getType().equals(activityType))
                            .collect(Collectors.toList()));
            }
            
            model.addObject("activities", activityList);
        }
        
        LOGGER.info("CalculoMentalController.completeActity.out");
        return model;
    }

}
