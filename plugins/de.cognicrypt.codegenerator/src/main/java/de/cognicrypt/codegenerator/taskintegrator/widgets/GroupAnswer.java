package de.cognicrypt.codegenerator.taskintegrator.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.cognicrypt.codegenerator.question.Answer;

public class GroupAnswer extends Group {

	public Text txtAnswer;
	private Answer answer;
	public ArrayList<Answer> answers;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public GroupAnswer(Composite parent, int style, Answer answerParam, boolean showRemoveButton) {
		super(parent, style);
		setAnswer(answerParam);

		txtAnswer = new Text(this, SWT.BORDER);
		if (answer.getValue() != null) {
			txtAnswer.setText(answer.getValue());
		}
		txtAnswer.setBounds(3, 3, 606, 29);

		txtAnswer.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				answer.setValue(txtAnswer.getText());
			}
		});

		Button defaultAnswer = new Button(this, SWT.RADIO);
		defaultAnswer.setBounds(613, 3, 128, 31);
		ArrayList<Button> btnList = ((CompositeToHoldSmallerUIElements) defaultAnswer.getParent().getParent().getParent()).getDefaulAnswerBtnList();
		btnList.add(defaultAnswer);
		defaultAnswer.setText("Default Answer");
		if (answer.isDefaultAnswer()) {
			defaultAnswer.setSelection(true);
		}
		defaultAnswer.setToolTipText("the answer that will be automatically selected when question appears for the first time");

		defaultAnswer.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				/**
				 * When user changes the default answer the following loop removes the previous selection and then current selected default answer value is set to true
				 */
				for (Button btn : ((CompositeToHoldSmallerUIElements) defaultAnswer.getParent().getParent().getParent()).getDefaulAnswerBtnList()) {
					btn.setSelection(false);
				}
				defaultAnswer.setSelection(true);

				/**
				 * sets the default answer to true for the current answer and for all other answer to false
				 */
				for (Answer ans : ((CompositeToHoldSmallerUIElements) defaultAnswer.getParent().getParent().getParent()).getListOfAllAnswer()) {
					if (ans.equals(answer)) {
						answer.setDefaultAnswer(true);
					} else {
						ans.setDefaultAnswer(false);
					}
				}

			}
		});

		if (showRemoveButton) {
			Button btnRemove = new Button(this, SWT.NONE);
			btnRemove.setBounds(746, 3, 80, 31);
			btnRemove.setText("Remove");
			btnRemove.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					MessageBox confirmationMessageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
					confirmationMessageBox.setMessage("This information will be lost. Do you really want to delete?");
					confirmationMessageBox.setText("Deleting answer");
					int response = confirmationMessageBox.open();
					if (response == SWT.YES) {
						((CompositeToHoldSmallerUIElements) btnRemove.getParent().getParent().getParent()).deleteAnswer(answer);
						btnList.remove(defaultAnswer);
					}

				}

			});
		}
	}

	/**
	 * @return the answer text
	 */
	/*
	 * public String retrieveAnswer(){ getAnswer=txtAnswer.getText(); return getAnswer; }
	 *//**
		 * set the answer text
		 *//*
		 * public void setAnswerValue(){ answer.setValue(this.retrieveAnswer()); }
		 */

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the answer
	 */
	public Answer getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *        the answer to set
	 */
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
}
