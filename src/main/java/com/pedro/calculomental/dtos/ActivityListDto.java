package com.pedro.calculomental.dtos;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class ActivityListDto.
 */
public class ActivityListDto implements Serializable{

	/** The Constant serialVersionUID. */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	/** The available activities. */
	private List<ActivityDto> availableActivities;
	
	/** The complete activities. */
	private List<ActivityDto> completedActivities;

	/**
	 * Gets the available activities.
	 *
	 * @return the availableActivities (all activities)
	 */
	public List<ActivityDto> getAvailableActivities() {
		return availableActivities;
	}

	/**
	 * Sets the available activities.
	 *
	 * @param availableActivities the availableActivities to set
	 */
	public void setAvailableActivities(List<ActivityDto> availableActivities) {
		this.availableActivities = availableActivities;
	}

	/**
	 * Gets the complete activities.
	 *
	 * @return the completedActivities
	 */
	public List<ActivityDto> getCompletedActivities() {
		return completedActivities;
	}

	/**
	 * Sets the completed activities.
	 *
	 * @param completedActivities the completeActivities to set
	 */
	public void setCompletedActivities(List<ActivityDto> completedActivities) {
		this.completedActivities = completedActivities;
	}
}
