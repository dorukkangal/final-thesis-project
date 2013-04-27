package tr.edu.gsu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.user.StudentExam;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;

import com.invient.vaadin.charts.Color;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientCharts.DecimalPoint;
import com.invient.vaadin.charts.InvientCharts.SeriesType;
import com.invient.vaadin.charts.InvientCharts.XYSeries;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitle;
import com.invient.vaadin.charts.InvientChartsConfig.CategoryAxis;
import com.invient.vaadin.charts.InvientChartsConfig.NumberYAxis;
import com.invient.vaadin.charts.InvientChartsConfig.PointConfig;
import com.invient.vaadin.charts.InvientChartsConfig.Tooltip;
import com.invient.vaadin.charts.InvientChartsConfig.XAxis;
import com.invient.vaadin.charts.InvientChartsConfig.YAxis;

public class ChartUtil {

	public static InvientCharts buildExamResultChart(List<StudentExam> examResults) {
		InvientCharts examResultChart = new InvientCharts(getPieChartConfig(Messages.getValue(GoesConstants.EXAM_RESULT)));
		if (examResults != null && !examResults.isEmpty()) {
			XYSeries series = new XYSeries("");

			int totalPoint = 0;
			Set<Question> questions = examResults.get(0).getExam().getQuestions();
			for (Question question : questions)
				totalPoint += question.getPoint();

			int truePoint = 0;
			int falsePoint = 0;
			for (StudentExam result : examResults) {
				int questionPoint = result.getQuestion().getPoint();
				if (result.isTrue())
					truePoint += questionPoint;
				else
					falsePoint += questionPoint;
			}
			int emptyPoint = totalPoint - truePoint - falsePoint;

			PointConfig config;
			LinkedHashSet<DecimalPoint> points = new LinkedHashSet<DecimalPoint>();

			config = new PointConfig(new Color.RGB(97, 193, 20));
			config.setSliced(true);
			points.add(new DecimalPoint(series, Messages.getValue(GoesConstants.QUESTION_TRUE), truePoint, config));

			config = new PointConfig(new Color.RGB(189, 26, 26));
			config.setSliced(false);
			points.add(new DecimalPoint(series, Messages.getValue(GoesConstants.QUESTION_FALSE), falsePoint, config));

			config = new PointConfig(new Color.RGB(205, 106, 0));
			config.setSliced(false);
			points.add(new DecimalPoint(series, Messages.getValue(GoesConstants.QUESTION_EMPTY), emptyPoint, config));

			series.setSeriesPoints(points);
			examResultChart.addSeries(series);
		}
		return examResultChart;
	}

	public static InvientCharts buildExamChart() {
		InvientCharts examChart = new InvientCharts(getPieChartConfig(Messages.getValue(GoesConstants.DASHBOARD_EXAM_CREATION_RATE)));

		XYSeries series = new XYSeries("");
		LinkedHashSet<DecimalPoint> points = new LinkedHashSet<DecimalPoint>();
		List<Exam> examList = AppContext.getExamService().findAll();
		Map<Teacher, Integer> teacherExamCountPairs = new HashMap<Teacher, Integer>();
		for (Exam exam : examList) {
			Teacher teacher = exam.getCreator();
			Integer count = teacherExamCountPairs.get(teacher);
			teacherExamCountPairs.put(teacher, ((count == null) ? 1 : ++count));
		}
		Teacher currentTeacher = (Teacher) AppContext.getSession().getUser();
		for (Map.Entry<Teacher, Integer> pair : teacherExamCountPairs.entrySet()) {
			String fullName = pair.getKey().getFullName();
			Random r = new Random();
			PointConfig config = new PointConfig(new Color.RGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
			config.setSliced(!pair.getKey().getId().equals(currentTeacher.getId()));
			points.add(new DecimalPoint(series, fullName, pair.getValue(), config));
		}

		series.setSeriesPoints(points);
		examChart.addSeries(series);
		return examChart;
	}

	public static InvientCharts buildQuestionChart() {

		List<Question> questionList = AppContext.getQuestionService().findAll();
		Map<Teacher, Integer> teacherQuestionCountPairs = new HashMap<Teacher, Integer>();
		for (Question question : questionList) {
			Teacher teacher = question.getCreator();
			Integer count = teacherQuestionCountPairs.get(teacher);
			teacherQuestionCountPairs.put(teacher, ((count == null) ? 1 : ++count));
		}
		List<String> teacherNames = new ArrayList<String>();
		for (Teacher teacher : teacherQuestionCountPairs.keySet())
			teacherNames.add(teacher.getFullName());
		InvientCharts questionChart = new InvientCharts(getColumnChartConfig(Messages.getValue(GoesConstants.DASHBOARD_QUESTION_CREATION_RATE), teacherNames,
				Messages.getValue(GoesConstants.DASHBOARD_TEACHERS), Messages.getValue(GoesConstants.DASHBOARD_QUESTION_NUMBER)));

		XYSeries series = new XYSeries("");
		LinkedHashSet<DecimalPoint> points = new LinkedHashSet<DecimalPoint>();
		Teacher currentTeacher = (Teacher) AppContext.getSession().getUser();
		for (Map.Entry<Teacher, Integer> pair : teacherQuestionCountPairs.entrySet()) {
			String fullName = pair.getKey().getFullName();
			Random r = new Random();
			PointConfig config = new PointConfig(new Color.RGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
			config.setSliced(!pair.getKey().getId().equals(currentTeacher.getId()));
			points.add(new DecimalPoint(series, fullName, pair.getValue(), config));
		}

		series.setSeriesPoints(points);
		questionChart.addSeries(series);
		return questionChart;
	}

	public static InvientChartsConfig getPieChartConfig(String title) {
		InvientChartsConfig pieChartConfig = new InvientChartsConfig();
		pieChartConfig.getGeneralChartConfig().setType(SeriesType.PIE);
		pieChartConfig.getTitle().setText(title);

		Tooltip tooltip = new Tooltip();
		tooltip.setFormatterJsFunc("function() { return '<b>' + this.point.name +' </b>: ' + this.y + '' ; }");
		pieChartConfig.setTooltip(tooltip);
		pieChartConfig.getCredit().setEnabled(false);
		return pieChartConfig;
	}

	public static InvientChartsConfig getColumnChartConfig(String title, List<String> categories, String xAxisTitle, String yAxisTitle) {
		InvientChartsConfig columnChartConfig = new InvientChartsConfig();
		columnChartConfig.getGeneralChartConfig().setType(SeriesType.COLUMN);
		columnChartConfig.getTitle().setText(title);

		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(categories);
		xAxis.setTitle(new AxisTitle(xAxisTitle));

		LinkedHashSet<XAxis> xAxesSet = new LinkedHashSet<InvientChartsConfig.XAxis>();
		xAxesSet.add(xAxis);
		columnChartConfig.setXAxes(xAxesSet);

		NumberYAxis yAxis = new NumberYAxis();
		yAxis.setMin(0.0);
		yAxis.setTitle(new AxisTitle(yAxisTitle));

		LinkedHashSet<YAxis> yAxesSet = new LinkedHashSet<InvientChartsConfig.YAxis>();
		yAxesSet.add(yAxis);
		columnChartConfig.setYAxes(yAxesSet);

		Tooltip tooltip = new Tooltip();
		tooltip.setFormatterJsFunc("function() { return '<br>' + this.point.name + '</br>: ' + this.y + ''; }");
		columnChartConfig.setTooltip(tooltip);
		columnChartConfig.getCredit().setEnabled(false);
		return columnChartConfig;
	}

}
