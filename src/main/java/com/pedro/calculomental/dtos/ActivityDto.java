package com.pedro.calculomental.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The Class Activity.
 */
public class ActivityDto extends BaseDto{

	/** Kind of activity / Activity type */
	public static enum ActivityType {
		SUMA("suma"), RESTA("resta"), MULTIPLICACION("multiplicacion"), DIVISION("division");
		
		/** The operation. */
		private String operation;
		
		/**
		 * @return the operation
		 */
		public String getOperation() {
			return operation;
		}
		
		/**
		 * Instantiates a new activity type.
		 *
		 * @param operation the operation
		 */
		private ActivityType(String operation) {
			this.operation = operation;
		}

		/**
		 * Gets the by operation.
		 *
		 * @param operation the operation
		 * @return the activity type parameter.
		 * @throws RuntimeException the runtime exception
		 */
		public static ActivityType getByOperation(String operation) throws RuntimeException {
			for (ActivityType item: ActivityType.values())
				if (operation.toLowerCase().equals(item.getOperation()))
					return item;
			throw new RuntimeException(String.format("ActivityDto.ActivityType - %s - Invalid operationType", operation));
		}
		
		
	}
	
	/** Activities difficulty. To be implemented in v2 */
	public static enum Leverage {
		PRIMER_CICLO, SEGUNDO_CICLO, TERCER_CICLO, NO_APLICA;
	}
	
	
	/** The Constant serialVersionUID. */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	/** The level. To be implemented in v2 */
	private Leverage level;
	
	/** The type. */
	private ActivityType type;
	
	/** The questions. */
	private List<String> questions;


	/**
	 * Gets the Activity Number without the operation reference letters (first two letters).
	 *
	 * @return Activity Number
	 */
	public String getActivityNumber() {
		return this.getId().substring(2);
	}

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public Leverage getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the level to set
	 */
	public void setLevel(Leverage level) {
		this.level = level;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public ActivityType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the type to set
	 */
	public void setType(ActivityType type) {
		this.type = type;
	}

	/**
	 * Gets the questions.
	 *
	 * @return the questions
	 */
	public List<String> getQuestions() {
		return questions;
	}

	/**
	 * Sets the questions.
	 *
	 * @param questions the questions to set
	 */
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
}
