package de.cognicrypt.codegenerator.taskintegrator.wizard;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.cognicrypt.codegenerator.taskintegrator.models.ClaferConstraint;
import de.cognicrypt.codegenerator.taskintegrator.models.ClaferFeature;
import de.cognicrypt.codegenerator.taskintegrator.models.ClaferModel;
import de.cognicrypt.codegenerator.taskintegrator.models.FeatureProperty;
import de.cognicrypt.codegenerator.taskintegrator.widgets.FeaturePropertiesContentProvider;
import de.cognicrypt.codegenerator.taskintegrator.widgets.FeaturePropertiesLabelProvider;

public class ClaferConstraintDialog extends Dialog {

	private Text text;
	private ClaferConstraint cfrConstraint;

	private ClaferFeature currentFeature;
	private ClaferModel claferModel;

	private ClaferConstraint modifiedConstraint;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public ClaferConstraintDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.RESIZE | SWT.TITLE);

		cfrConstraint = new ClaferConstraint();
	}

	public ClaferConstraintDialog(Shell parentShell, ClaferFeature currentFeature, ClaferModel claferModel) {
		this(parentShell);
		this.currentFeature = currentFeature;
		this.claferModel = claferModel;
	}

	public ClaferConstraintDialog(Shell parentShell, ClaferFeature currentFeature, ClaferModel claferModel, ClaferConstraint modifiedConstraint) {
		this(parentShell, currentFeature, claferModel);
		this.modifiedConstraint = modifiedConstraint;
	}

	public ClaferFeature getClafer() {
		return currentFeature;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
		container.setLayout(gl_container);

		getShell().setMinimumSize(600, 400);

		TreeViewer treeViewer = new TreeViewer(container);
		treeViewer.setContentProvider(new FeaturePropertiesContentProvider());
		treeViewer.setLabelProvider(new FeaturePropertiesLabelProvider());

		// create a temporary clafer model that contains the current model as well as the feature currently being created
		ClaferModel tempModel = claferModel.clone();
		tempModel.add(currentFeature);

		treeViewer.setInput(tempModel);
		treeViewer.expandAll();
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeViewer.getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (treeViewer.getSelection() instanceof TreeSelection) {
					TreeSelection ts = (TreeSelection) treeViewer.getSelection();

					// toggle expansion if feature name clicked
					if (ts.getFirstElement() instanceof ClaferFeature) {
						Object selectedElem = ts.getFirstElement();
						treeViewer.setExpandedState(selectedElem, !treeViewer.getExpandedState(selectedElem));
					}
					// add to text field if property name clicked
					else if (ts.getFirstElement() instanceof FeatureProperty) {
						FeatureProperty propertyClicked = (FeatureProperty) ts.getFirstElement();
						ClaferFeature parentFeature = ((ClaferModel) treeViewer.getInput()).getParentFeatureOfProperty(propertyClicked);
						text.insert(parentFeature.getFeatureName());
						text.insert(".");
						text.insert(propertyClicked.getPropertyName());
					}
				}
				super.mouseDoubleClick(e);
			}
		});

		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		RowLayout rl_group = new RowLayout(SWT.HORIZONTAL);
		group.setLayout(rl_group);

		ArrayList<Entry<String, String>> buttonContents = new ArrayList<>();
		buttonContents.add(new SimpleEntry<String, String>("NOT", " !"));
		buttonContents.add(new SimpleEntry<String, String>("EQUALS", "="));
		buttonContents.add(new SimpleEntry<String, String>("AND", " AND "));
		buttonContents.add(new SimpleEntry<String, String>("OR", " OR "));
		buttonContents.add(new SimpleEntry<String, String>("IMPLIES", " => "));
		buttonContents.add(new SimpleEntry<String, String>("(", " ("));
		buttonContents.add(new SimpleEntry<String, String>(")", ") "));
		buttonContents.add(new SimpleEntry<String, String>(">", " > "));
		buttonContents.add(new SimpleEntry<String, String>("<", " < "));

		for (Entry<String, String> btn : buttonContents) {
			Button newButton = new Button(group, SWT.PUSH);
			newButton.setText(btn.getKey());
			newButton.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					text.insert(btn.getValue());
					text.setFocus();
				}
			});
		}

		text = new Text(container, SWT.BORDER | SWT.WRAP | SWT.SINGLE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		if (modifiedConstraint != null) {
			text.setText(modifiedConstraint.getConstraint());
		}

		return container;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 600);
	}

	@Override
	protected void okPressed() {
		cfrConstraint.setConstraint(text.getText());
		super.okPressed();
	}

	public ClaferConstraint getResult() {
		return cfrConstraint;
	}

}