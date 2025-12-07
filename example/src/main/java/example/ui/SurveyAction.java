package example.ui;

import com.googlecode.scopeplugin.ScopeType;
import com.googlecode.scopeplugin.annotations.Begin;
import com.googlecode.scopeplugin.annotations.End;
import com.googlecode.scopeplugin.annotations.In;
import com.googlecode.scopeplugin.annotations.Out;
import org.apache.struts2.ActionSupport;


/** */
public class SurveyAction extends ActionSupport {
	@In (scope=ScopeType.CONVERSATION)
	@Out (scope=ScopeType.CONVERSATION)
	private SurveyBean surveyBean;
	
	@Begin
	public String firstPage() {
		surveyBean = new SurveyBean();
		return SUCCESS;
	}
	
	public String secondPage() {
		return SUCCESS;
	}
	
	public String thirdPage() {
		return SUCCESS;
	}

	@End
	public String summaryPage() {
		return SUCCESS;
	}

	public SurveyBean getSurveyBean() {
		return surveyBean;
	}

	public void setSurveyBean(SurveyBean surveyBean) {
		this.surveyBean = surveyBean;
	}
}
