package crossing.e1.configurator.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clafer.ast.AstConcreteClafer;
import org.clafer.instance.InstanceClafer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.*;

import crossing.e1.featuremodel.clafer.ClaferModel;
import crossing.e1.featuremodel.clafer.InstanceGenerator;
import crossing.e1.featuremodel.clafer.ParseClafer;
import crossing.e1.featuremodel.clafer.StringLableMapper;

/**
 * @author Ram
 *
 */

public class TaskSelectionPage extends WizardPage {

	private Composite container;
	private ComboViewer algorithmClass;
	private Label label1;
	String value = "";
	private ClaferModel model;

	public TaskSelectionPage(ClaferModel claferModel) {
		super("Select Task");
		setTitle("Configure");
		setDescription("Here the user selects her options and security levels");
		this.model = claferModel;

	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		container.setLayout(layout);

		label1 = new Label(container, SWT.NONE);
		label1.setText("Select Task");
		Set<String> getSuperClafer = model.getTaskList(model.getModel())
				.keySet();
		algorithmClass = new ComboViewer(container, SWT.COMPOSITION_SELECTION);
		algorithmClass.setContentProvider(ArrayContentProvider.getInstance());
		algorithmClass.setInput(getSuperClafer);
		if (getSuperClafer.size() > 0) {
			algorithmClass.setSelection(new StructuredSelection(getSuperClafer
					.toArray()[0]));
		} else {
			setPageComplete(false);
			algorithmClass.setSelection(new StructuredSelection("no task"));

		}
		algorithmClass.setLabelProvider((new LabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		}));
		algorithmClass
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();

						ParseClafer parser= new ParseClafer();
						String b = (String) selection.getFirstElement()
								.toString();
						StringLableMapper.resetProperties();
						parser.setConstraintClafers(StringLableMapper.getTaskLables()
								.get(b));
						setValue(b);

					}

				});

		setControl(container);
		;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
			this.value = value;
	}

}
