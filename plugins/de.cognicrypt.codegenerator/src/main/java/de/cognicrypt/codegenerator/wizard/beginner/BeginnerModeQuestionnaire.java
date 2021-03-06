/********************************************************************************
 * Copyright (c) 2015-2018 TU Darmstadt
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/

package de.cognicrypt.codegenerator.wizard.beginner;

import java.util.List;

import de.cognicrypt.codegenerator.question.Page;
import de.cognicrypt.codegenerator.question.Question;
import de.cognicrypt.codegenerator.question.QuestionsJSONReader;
import de.cognicrypt.codegenerator.tasks.Task;

public class BeginnerModeQuestionnaire {

	private final List<Question> questionList;
	private final List<Page> pageList;
	private Task task;
	private int pageID;

	/**
	 *
	 * @param task
	 * @param filePath
	 */
	public BeginnerModeQuestionnaire(final Task task, final String filePath) {
		this.task = task;
		this.pageList = (new QuestionsJSONReader()).getPages(filePath);
		this.pageID = 0;

		this.questionList = null;
	}

	/**
	 * Added this method to get specific questions. This functionality was created to replace the code when handling buttons as 'Questions'.
	 * 
	 * @param questionID
	 * @return The requested question.
	 */
	public Question getQuestionByID(final int questionID) {

		for (final Page page : this.pageList) {
			for (final Question question : page.getContent()) {
				if (question.getId() == questionID) {
					return question;
				}
			}
		}
		return null;
	}

	public List<Page> getQuestionnaire() throws NullPointerException {
		return this.pageList;
	}

	public List<Question> getQuestionList() {
		return this.questionList;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

	/**
	 *
	 * @param pageID
	 * @return Return the page at pageID.
	 */
	public Page getPageByID(final int pageID) {
		return this.pageList.get(pageID);
	}

	/**
	 *
	 * @return Return the list of pages.
	 * @throws NullPointerException
	 */
	public List<Page> getPages() throws NullPointerException {
		return this.pageList;
	}

	/**
	 *
	 * @return Return the next page.
	 */
	public Page nextPage() {
		return this.pageList.get(this.pageID++);
	}

	/**
	 *
	 * @return Return the previous page.
	 */
	public Page previousPage() {
		return this.pageList.get(--this.pageID);
	}

	/**
	 *
	 * @param pageID
	 * @return Return the page that has been set.
	 */
	public Page setPageByID(final int pageID) {
		this.pageID = pageID;
		return this.pageList.get(this.pageID);
	}

	/**
	 *
	 * @return Whether this is the first page.
	 */
	public boolean isFirstPage() {
		return this.pageID == 0;
	}

	/**
	 *
	 * @return Return whether there are more pages.
	 */
	public boolean hasMorePages() {
		return this.pageID < getPages().size();
	}

	/**
	 *
	 * @return Return the current pageID.
	 */
	public int getCurrentPageID() {
		return this.pageID;
	}

}
