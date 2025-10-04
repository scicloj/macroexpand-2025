^:kindly/hide-code
(ns macroexpand-noj
  (:require [scicloj.kindly.v4.kind :as kind]))

;; ## Macroexpand-Noj - growing the Noj ecosystem
;; 
;; **📅 October 17-18, 2025 | Online Conference**
;;
;; The Macroexpand-Noj conference is focused on the growth of the [Noj](https://scicloj.github.io/noj) toolkit for data science. Noj is a Clojure toolkit that brings together data processing, visualization, and scientific computing libraries into a cohesive whole. This conference is one of the [Macroexpand 2025](./) pair of conferences organized by [Scicloj](https://scicloj.github.io/).
;; 
;; ### About the Conference
;;
;; This two-day online event brings together practitioners, library authors, and data enthusiasts to share knowledge, showcase projects, and collaborate on growing Clojure's data science capabilities. Whether you're analyzing real-world datasets, building new tools, or documenting existing libraries, this conference is for you.
;;
;; ### Call for Proposals
;; 
;; ### Speakers
;; 
;; The Call for Proposals is already closed, but if you have any late ideas you wish to discuss, please reach out.
;;
;; Please review our [speaker guidelines](./speakers.html) for detailed requirements and recommendations.
;;
;; ### Preferred Topics
;; 
;; The following have been prioritized in reviewing the talk proposals:
;; 
;; * **Tutorial Talks** (Highly Prioritized) - Beginner-friendly, hands-on tutorials introducing Noj components, data science workflows, and practical techniques
;; * **Documentation** - Making libraries, theory, and methods accessible to the community
;; * **Real-world Analysis** - Practical data science examples and case studies (including follow-ups from [SciNoj Light](https://scicloj.github.io/scinoj-light-1/))
;; * **Tools & Libraries** - New features, integrations, and ecosystem improvements
;; * **Data Engineering** - Processing pipelines, ETL, and data infrastructure
;; * **Visualization** - Interactive graphics, dashboards, and visual analytics
;; * **Scientific Computing** - Numerical methods, statistics, and domain-specific applications
;; * **Machine Learning** - Models, workflows, and ML engineering with Clojure
;;
;; ### Target Audience
;;
;; General Clojure programmers interested in data science, from beginners exploring data tools to experienced practitioners sharing advanced techniques. Talks should be accessible while providing depth and practical value.
;;
;; ### Schedule (Draft)
;;
;; All times are in UTC. The schedule below is a proof-of-concept showing available time slots.

^:kindly/hide-code
(def schedule-data
  {:day1 {:date "Thursday, October 17, 2025"
          :slots {"08:00-09:00" "Opening & Welcome"
                  "09:00-10:00" "TBD"
                  "10:00-11:00" "TBD"
                  "11:00-12:00" "TBD"
                  "12:00-13:00" "Break"
                  "13:00-14:00" "TBD"
                  "14:00-15:00" "TBD"
                  "15:00-16:00" "TBD"
                  "16:00-17:00" "Break"
                  "17:00-18:00" "TBD"
                  "18:00-19:00" "TBD"
                  "19:00-20:00" "Closing Day 1"}}
   :day2 {:date "Friday, October 18, 2025"
          :slots {"08:00-09:00" "Welcome Day 2"
                  "09:00-10:00" "TBD"
                  "10:00-11:00" "TBD"
                  "11:00-12:00" "TBD"
                  "12:00-13:00" "Break"
                  "13:00-14:00" "TBD"
                  "14:00-15:00" "TBD"
                  "15:00-16:00" "TBD"
                  "16:00-17:00" "Break"
                  "17:00-18:00" "TBD"
                  "18:00-19:00" "TBD"
                  "19:00-20:00" "Conference Wrap-up"}}})

^:kindly/hide-code
(defn schedule-table [day-data]
  (kind/hiccup
   [:table {:style "width: 100%; border-collapse: collapse; margin: 1rem 0;"}
    [:thead
     [:tr
      [:th {:style "border: 1px solid #ddd; padding: 12px; background-color: #f8f9fa; text-align: left; font-weight: bold;"} "Time (UTC)"]
      [:th {:style "border: 1px solid #ddd; padding: 12px; background-color: #f8f9fa; text-align: left; font-weight: bold;"} "Session"]]]
    [:tbody
     (for [[time-slot session] (sort (:slots day-data))]
       [:tr
        [:td {:style "border: 1px solid #ddd; padding: 12px; font-family: monospace; background-color: #f8f9fa;"} time-slot]
        [:td {:style "border: 1px solid #ddd; padding: 12px;"}
         (if (= session "TBD")
           [:em {:style "color: #666;"} session]
           session)]])]]))

^:kindly/hide-code
(kind/hiccup
 [:div
  [:h4 {:style "margin-top: 2rem; color: #2c5282;"} (:date (:day1 schedule-data))]
  (schedule-table (:day1 schedule-data))

  [:h4 {:style "margin-top: 2rem; color: #2c5282;"} (:date (:day2 schedule-data))]
  (schedule-table (:day2 schedule-data))

  [:p {:style "margin-top: 1rem; font-style: italic; color: #666;"}
   "* Schedule is subject to change. Final schedule with confirmed speakers will be published closer to the conference date."]])

;; ### Connect & Discuss
;;
;; Join the conversation at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/) where we coordinate projects and help each other with data science in Clojure.
;;
;; [Questions? Let's talk](https://scicloj.github.io/docs/community/contact/){class="btn btn-gradient" target="_blank"}

^:kindly/hide-code
(kind/hiccup
 [:div
  [:div {:style "margin: 2rem 0; text-align: center;"}
   [:button {:id "global-timezone-toggle"
             :style "background: #2c5282; color: white; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 0.9rem;"
             :onclick "toggleAllTimezones()"} "Show Times in My Timezone"]
   [:div {:id "timezone-info" :style "margin-top: 0.5rem; color: #666; font-style: italic; font-size: 0.85rem;"}]]

  [:script {:type "text/javascript"}
   "
function toggleAllTimezones() {
  const button = document.getElementById('global-timezone-toggle');
  const timezoneInfo = document.getElementById('timezone-info');
  const tables = document.querySelectorAll('.schedule-table');
  
  const isShowingUTC = button.textContent.includes('My Timezone');
  
  if (isShowingUTC) {
    const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    
    // Update all schedule tables
    tables.forEach(table => {
      const timeCells = table.querySelectorAll('.time-cell');
      
      timeCells.forEach(cell => {
        const dateStr = cell.dataset.date;
        const timeRange = cell.dataset.timeRange;
        
        if (dateStr && timeRange) {
          const [startTime, endTime] = timeRange.split('-');
          const [startHour] = startTime.split(':');
          const [endHour] = endTime.split(':');
          
          // Parse date string like 'Thursday, October 17, 2025'
          const dateMatch = dateStr.match(/(\\w+), (\\w+) (\\d+), (\\d+)/);
          if (dateMatch) {
            const [, , month, day, year] = dateMatch;
            const monthNames = ['January','February','March','April','May','June',
                               'July','August','September','October','November','December'];
            const monthNum = monthNames.indexOf(month);
            
            if (monthNum !== -1) {
              const startDate = new Date(year, monthNum, day, parseInt(startHour), 0);
              const endDate = new Date(year, monthNum, day, parseInt(endHour), 0);
              
              const localStartTime = startDate.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: false,
                timeZone: userTimezone
              });
              
              const localEndTime = endDate.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit', 
                hour12: false,
                timeZone: userTimezone
              });
              
              cell.textContent = localStartTime + '-' + localEndTime;
            }
          }
        }
      });
      
      // Update table headers
      const headers = table.querySelectorAll('th');
      if (headers.length > 0) {
        headers[0].textContent = 'Time (Local)';
      }
    });
    
    button.textContent = 'Show Times in UTC';
    timezoneInfo.textContent = 'Times shown in ' + userTimezone + ' timezone';
  } else {
    // Reset to UTC
    tables.forEach(table => {
      const timeCells = table.querySelectorAll('.time-cell');
      
      timeCells.forEach(cell => {
        const timeRange = cell.dataset.timeRange;
        if (timeRange) {
          cell.textContent = timeRange;
        }
      });
      
      // Update table headers back to UTC
      const headers = table.querySelectorAll('th');
      if (headers.length > 0) {
        headers[0].textContent = 'Time (UTC)';
      }
    });
    
    button.textContent = 'Show Times in My Timezone';
    timezoneInfo.textContent = '';
  }
}

// Add class to existing tables for easier targeting
document.addEventListener('DOMContentLoaded', function() {
  const tables = document.querySelectorAll('table');
  tables.forEach(table => {
    if (table.querySelector('th') && table.querySelector('th').textContent.includes('Time')) {
      table.classList.add('schedule-table');
      
      // Add data attributes to time cells
      const timeCells = table.querySelectorAll('td');
      timeCells.forEach((cell, index) => {
        if (index % 2 === 0) { // Time columns (every other cell)
          const timeText = cell.textContent.trim();
          if (timeText.match(/\\d{2}:\\d{2}-\\d{2}:\\d{2}/)) {
            cell.classList.add('time-cell');
            cell.dataset.timeRange = timeText;
            
            // Find the date from the preceding h4
            let dateElement = cell.closest('table').previousElementSibling;
            while (dateElement && dateElement.tagName !== 'H4') {
              dateElement = dateElement.previousElementSibling;
            }
            if (dateElement) {
              cell.dataset.date = dateElement.textContent.trim();
            }
          }
        }
      });
    }
  });
});
"]])
