package tr.edu.gsu.view.page.exam;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.BasePage;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.CalendarEvent;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.Calendar.TimeFormat;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.DateClickEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClick;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClickHandler;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.WeekClick;
import com.vaadin.addon.calendar.ui.handler.BasicDateClickHandler;
import com.vaadin.addon.calendar.ui.handler.BasicWeekClickHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ExamView extends BasePage implements ClickListener {

	private HorizontalLayout topLayout;
	private Button previous;
	private Button next;
	private Button monthly;
	private Button weekly;
	private Button daily;

	private GregorianCalendar calendar;
	private Calendar calendarComponent;
	private BasicWeekClickHandler weekClickHandler;
	private BasicDateClickHandler dateClickHandler;
	private ExamEventClickHandler examEventClickHandler;

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		topLayout = buildTopLayout();
		mainLayout.addComponent(topLayout);

		calendarComponent = buildCalendar();
		mainLayout.addComponent(calendarComponent);
		mainLayout.setExpandRatio(calendarComponent, 1.0f);

		return mainLayout;
	}

	private HorizontalLayout buildTopLayout() {
		topLayout = new HorizontalLayout();
		topLayout.setWidth("100%");
		topLayout.setMargin(true, false, false, false);
		previous = new Button(Messages.getValue(GoesConstants.BUTTON_PREVIOUS), this);
		previous.setStyleName(Reindeer.BUTTON_LINK);
		topLayout.addComponent(previous);
		topLayout.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		topLayout.addComponent(hl);
		topLayout.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);

		monthly = new Button(Messages.getValue(GoesConstants.MONTHLY), this);
		monthly.setStyleName(Reindeer.BUTTON_LINK);
		hl.addComponent(monthly);

		weekly = new Button(Messages.getValue(GoesConstants.WEEKLY), this);
		weekly.setStyleName(Reindeer.BUTTON_LINK);
		hl.addComponent(weekly);

		daily = new Button(Messages.getValue(GoesConstants.DAILY), this);
		daily.setStyleName(Reindeer.BUTTON_LINK);
		hl.addComponent(daily);

		next = new Button(Messages.getValue(GoesConstants.BUTTON_NEXT), this);
		next.setStyleName(Reindeer.BUTTON_LINK);
		topLayout.addComponent(next);
		topLayout.setComponentAlignment(next, Alignment.MIDDLE_RIGHT);

		return topLayout;
	}

	private Calendar buildCalendar() {
		calendar = new GregorianCalendar();

		calendarComponent = new Calendar();
		calendarComponent.setSizeFull();
		calendarComponent.setLocale(session.getLocale());
		calendarComponent.setTimeFormat(TimeFormat.Format24H);

		switchToMonthlyView();
		addCalendarEventListeners();

		return calendarComponent;
	}

	private void addCalendarEventListeners() {
		weekClickHandler = new WeekClickHandler();
		calendarComponent.setHandler(weekClickHandler);

		dateClickHandler = new DateClickHandler();
		calendarComponent.setHandler(dateClickHandler);

		examEventClickHandler = new ExamEventClickHandler();
		calendarComponent.setHandler(examEventClickHandler);
	}

	private void switchToMonthlyView() {
		calendarComponent.setStartDate(getFirstDateOfCurrentMonth());
		calendarComponent.setEndDate(getLastDateOfCurrentMonth());

		previous.setVisible(true);
		next.setVisible(true);
		monthly.setEnabled(false);
		weekly.setEnabled(true);
		daily.setEnabled(true);
	}

	private void switchToWeeklyView() {
		previous.setVisible(false);
		next.setVisible(false);
		monthly.setEnabled(true);
		weekly.setEnabled(false);
		daily.setEnabled(true);
	}

	private void switchToDailyView() {
		previous.setVisible(false);
		next.setVisible(false);
		monthly.setEnabled(true);
		weekly.setEnabled(true);
		daily.setEnabled(false);
	}

	@Override
	public void refreshContent() {
		List<Exam> exams = AppContext.getExamService().findAll();
		for (Exam exam : exams) {
			ExamEvent examEvent = new ExamEvent(exam);
			calendarComponent.addEvent(examEvent);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();
		if (source == previous) {
			handlePreviousButtonClick();
		} else if (source == next) {
			handleNextButtonClick();
		} else if (source == monthly) {
			switchToMonthlyView();
		} else if (source == weekly) {
			calendar.setTime(new Date());
			weekClickHandler.weekClick(new WeekClick(calendarComponent, calendar.get(GregorianCalendar.WEEK_OF_YEAR), calendar.get(GregorianCalendar.YEAR)));
		} else if (source == daily) {
			dateClickHandler.dateClick(new DateClickEvent(calendarComponent, new Date()));
		}
	}

	private void handleNextButtonClick() {
		rollMonth(1);
	}

	private void handlePreviousButtonClick() {
		rollMonth(-1);
	}

	private void rollMonth(int direction) {
		Date start = calendarComponent.getStartDate();
		calendar.setTime(start);
		calendar.add(GregorianCalendar.MONTH, direction);
		calendarComponent.setStartDate(calendar.getTime());

		Date end = calendarComponent.getEndDate();
		calendar.setTime(end);
		calendar.add(GregorianCalendar.MONTH, direction);
		calendarComponent.setEndDate(calendar.getTime());
	}

	private class ExamEvent extends BasicEvent implements CalendarEvent {
		private Exam exam;

		public ExamEvent(Exam exam) {
			this.exam = exam;
		}

		@Override
		public Date getStart() {
			return exam.getStart();
		}

		@Override
		public Date getEnd() {
			return exam.getEnd();
		}

		@Override
		public String getCaption() {
			return exam.getName();
		}

		@Override
		public String getDescription() {
			return exam.getDescription();
		}

		@Override
		public String getStyleName() {
			int diffInDays = getDifferenceBetweenDates(exam.getStart(), new Date());
			if (diffInDays < 0)
				return "color1"; // yesil
			else if (diffInDays < 2)
				return "color3"; // kırmızı
			else if (diffInDays < 4)
				return "color4"; // turuncu
			else
				return "color2"; // mavi
		}

		@Override
		public boolean isAllDay() {
			return true;
		}

		public Exam getExam() {
			return exam;
		}
	}

	private class WeekClickHandler extends BasicWeekClickHandler {
		@Override
		public void weekClick(WeekClick event) {
			super.weekClick(event);
			switchToWeeklyView();
		}
	}

	private class DateClickHandler extends BasicDateClickHandler {
		@Override
		public void dateClick(DateClickEvent event) {
			super.dateClick(event);
			switchToDailyView();
		}
	}

	private class ExamEventClickHandler implements EventClickHandler {
		@Override
		public void eventClick(EventClick event) {
			CalendarEvent calendarEvent = event.getCalendarEvent();
			if (calendarEvent instanceof ExamEvent)
				mainWindow.showExamForm(((ExamEvent) calendarEvent).getExam());
		}
	}

	private int getDifferenceBetweenDates(Date startDate, Date endDate) {
		return (int) ((startDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
	}

	private Date getFirstDateOfCurrentMonth() {
		calendar.set(GregorianCalendar.DAY_OF_MONTH, calendar.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	private Date getLastDateOfCurrentMonth() {
		calendar.set(GregorianCalendar.DAY_OF_MONTH, calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
}
