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
;; A two-day online event for sharing practical data science experiences, showcasing Noj ecosystem tools, and advancing Clojure's data science capabilities through tutorials, case studies, and technical discussions. Designed for Clojure programmers at all levels interested in data science.
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

  [:p {:id "timezone-notice" :style "margin-top: 1rem; color: #666; font-size: 0.9rem;"}
   [:em "Times will be displayed in your local timezone"]]

  [:p {:style "margin-top: 0.5rem; font-style: italic; color: #666;"}
   "* Schedule is subject to change. Final schedule with confirmed speakers will be published closer to the conference date."]])

;; ### Connect & Discuss
;;
;; Join the conversation at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/) where we coordinate projects and help each other with data science in Clojure.

^:kindly/hide-code
(kind/hiccup
 [:div {:style "text-align: center; margin: 2rem 0;"}
  [:a {:href "https://scicloj.github.io/docs/community/contact/"
       :target "_blank"
       :class "btn btn-gradient"
       :style "display: inline-block; text-decoration: none;"}
   "Questions? Let's talk"]])

^:kindly/hide-code
(kind/hiccup
 [:script {:type "text/javascript"}
  "
// Automatically convert times to user's timezone on page load
document.addEventListener('DOMContentLoaded', function() {
  const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
  
  // Update timezone notice
  const timezoneNotice = document.getElementById('timezone-notice');
  if (timezoneNotice) {
    timezoneNotice.innerHTML = '<em>All times shown in ' + userTimezone + '</em>';
  }
  
  // Find all tables and process them
  const tables = document.querySelectorAll('table');
  
  tables.forEach(table => {
    const timeHeader = table.querySelector('th');
    if (timeHeader && timeHeader.textContent.includes('Time')) {
      // Update header to show timezone
      timeHeader.textContent = 'Time (' + userTimezone + ')';
      
      // Find date from previous H4 element
      let dateElement = table.previousElementSibling;
      while (dateElement && dateElement.tagName !== 'H4') {
        dateElement = dateElement.previousElementSibling;
      }
      
      if (dateElement) {
        const dateText = dateElement.textContent.trim();
        const dateMatch = dateText.match(/(\\w+), (\\w+) (\\d+), (\\d+)/);
        
        if (dateMatch) {
          const [, , month, day, year] = dateMatch;
          const monthNames = ['January','February','March','April','May','June',
                             'July','August','September','October','November','December'];
          const monthNum = monthNames.indexOf(month);
          
          if (monthNum !== -1) {
            const cells = table.querySelectorAll('td');
            for (let i = 0; i < cells.length; i += 2) {
              const timeCell = cells[i];
              const timeText = timeCell.textContent.trim();
              
              if (timeText.match(/\\d{2}:\\d{2}-\\d{2}:\\d{2}/)) {
                const [startTime, endTime] = timeText.split('-');
                const [startHour] = startTime.split(':');
                const [endHour] = endTime.split(':');
                
                // Create dates in UTC since schedule times are in UTC
                const startDate = new Date(Date.UTC(year, monthNum, day, parseInt(startHour), 0));
                const endDate = new Date(Date.UTC(year, monthNum, day, parseInt(endHour), 0));
                
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
                
                // Store UTC time as tooltip
                timeCell.title = 'UTC: ' + timeText;
                timeCell.style.cursor = 'help';
                
                timeCell.textContent = localStartTime + '-' + localEndTime;
              }
            }
          }
        }
      }
    }
  });
});
"])
