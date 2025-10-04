^:kindly/hide-code
(ns macroexpand-noj
  (:require [scicloj.kindly.v4.kind :as kind]
            [clojure.edn :as edn]
            [clojure.string :as str]))

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
(def conference-info
  (edn/read-string (slurp "info.edn")))

^:kindly/hide-code
(defn format-speaker-list [speakers people-data]
  (when (seq speakers)
    (->> speakers
         (map #(get-in people-data [% :full-name]))
         (filter some?)
         (str/join ", "))))

^:kindly/hide-code
(defn session-card-new [session-key session-data people-data]
  (let [session-title (:title session-data)
        speakers (:speakers session-data)
        speaker-names (format-speaker-list speakers people-data)
        abstract (:abstract session-data)
        speaker-images (when (seq speakers)
                         (for [speaker-key speakers]
                           (let [speaker-data (get people-data speaker-key)]
                             (some-> speaker-data
                                     :images
                                     first
                                     (as-> img
                                           [:img {:src (str "images/" img)
                                                  :alt (:full-name speaker-data)
                                                  :style "width: 60px; height: 60px; border-radius: 50%; object-fit: cover; margin-right: 8px;"}])))))]
    (kind/hiccup
     [:div {:style "margin-bottom: 1.5rem; border-left: 3px solid #ddd; padding-left: 1rem;"}

      [:details
       [:summary {:style "cursor: pointer; list-style: none; padding: 0.5rem 0;"}
        [:div {:style "display: flex; align-items: center; justify-content: space-between;"}
         [:h3 {:style "display: inline; margin: 0; font-size: 1.25rem;"}
          session-title]
         (when (seq (remove nil? speaker-images))
           [:div {:style "display: flex; align-items: center; flex-shrink: 0;"}
            (remove nil? speaker-images)])]]

       ;; Expanded content
       [:div {:style "padding: 1rem 0;"}
        ;; Abstract section
        [:div {:style "margin-bottom: 1.5rem;"}
         [:h4 "Abstract"]
         [:p abstract]]

        ;; Speaker details with images
        (when (seq speakers)
          [:div
           [:h4 (if (> (count speakers) 1) "Speakers" "Speaker")]
           (for [speaker-key speakers]
             (let [speaker-data (get people-data speaker-key)
                   speaker-image (some-> speaker-data
                                         :images
                                         first
                                         (as-> img
                                               [:img {:src (str "images/" img)
                                                      :alt (:full-name speaker-data)
                                                      :style "width: 80px; height: 80px; border-radius: 50%; object-fit: cover; margin-right: 1rem; float: left;"}]))]
               [:div {:key speaker-key :style "margin-bottom: 1.5rem; overflow: hidden;"}
                (when speaker-image speaker-image)
                [:div
                 [:h5 {:style "margin-bottom: 0.5rem;"} (:full-name speaker-data)]
                 [:p (:bio speaker-data)]]
                [:div {:style "clear: both;"}]]))])]]])))

^:kindly/hide-code
(defn session-key->display
  "Convert a session key to display content with collapsible card"
  [session-key sessions-data people-data]
  (cond
    (nil? session-key)
    (kind/hiccup [:div {:style "padding: 0.5rem; color: #666;"} "TBD"])

    (#{:break :closing-day-1 :conference-wrap-up :welcome-day-2} session-key)
    (let [session-data (get sessions-data session-key)
          title (:title session-data)]
      (kind/hiccup [:div {:style "padding: 0.5rem; color: #666; font-style: italic;"} title]))

    :else
    (let [session-data (get sessions-data session-key)]
      (if session-data
        (session-card-new session-key session-data people-data)
        (kind/hiccup [:div {:style "padding: 0.5rem; color: #666;"} "TBD"])))))

^:kindly/hide-code
(defn schedule-vector->slots
  "Convert schedule vector to time slot map with session cards"
  [schedule-vec sessions-data people-data]
  (let [start-hour 9]
    (into {}
          (map-indexed
           (fn [idx session-key]
             (let [hour (+ start-hour idx)]
               [(format "%02d:00-%02d:00" hour (inc hour))
                (session-key->display session-key sessions-data people-data)]))
           schedule-vec))))

^:kindly/hide-code
(defn date-string->day-name
  "Convert date string like '2025-10-17' to 'Thursday, October 17, 2025'"
  [date-str]
  (let [[year month day] (str/split date-str #"-")
        months ["January" "February" "March" "April" "May" "June"
                "July" "August" "September" "October" "November" "December"]
        ;; Calculate day of week using Zeller's formula (simplified)
        ;; For October 17, 2025 = Thursday, October 18, 2025 = Friday
        day-names ["Thursday" "Friday" "Saturday" "Sunday" "Monday" "Tuesday" "Wednesday"]
        month-name (nth months (dec (Integer/parseInt month)))
        day-num (Integer/parseInt day)]
    (if (= date-str "2025-10-17")
      (str "Thursday, " month-name " " day-num ", " year)
      (str "Friday, " month-name " " day-num ", " year))))

^:kindly/hide-code
(def schedule-data
  (let [noj-conf (get-in conference-info [:conferences :macroexpand-noj])
        dates (:dates noj-conf)
        [date1 date2] dates
        schedule (:schedule noj-conf)
        sessions (:sessions conference-info)
        people (:people conference-info)]
    {:day1 {:date (date-string->day-name date1)
            :slots (schedule-vector->slots (:day1 schedule) sessions people)}
     :day2 {:date (date-string->day-name date2)
            :slots (schedule-vector->slots (:day2 schedule) sessions people)}}))

^:kindly/hide-code
(defn columnar-schedule-table [day1-data day2-data]
  (let [day1-slots (sort (:slots day1-data))
        day2-slots (sort (:slots day2-data))
        all-time-slots (map first day1-slots)]
    (kind/hiccup
     [:table {:style "width: 100%; border-collapse: collapse; margin: 1rem 0;"}
      [:thead
       [:tr
        [:th {:style "border: 1px solid #ddd; padding: 12px; background-color: #f8f9fa; text-align: left; font-weight: bold; width: 15%;"} "Time (UTC)"]
        [:th {:style "border: 1px solid #ddd; padding: 12px; background-color: #f8f9fa; text-align: left; font-weight: bold; width: 42.5%;"}
         (:date day1-data)]
        [:th {:style "border: 1px solid #ddd; padding: 12px; background-color: #f8f9fa; text-align: left; font-weight: bold; width: 42.5%;"}
         (:date day2-data)]]]
      [:tbody
       (for [time-slot all-time-slots]
         (let [day1-session (get (into {} day1-slots) time-slot)
               day2-session (get (into {} day2-slots) time-slot)]
           [:tr
            [:td {:style "border: 1px solid #ddd; padding: 12px; font-family: monospace; background-color: #f8f9fa; vertical-align: top;"} time-slot]
            [:td {:style "border: 1px solid #ddd; padding: 8px; vertical-align: top;"}
             day1-session]
            [:td {:style "border: 1px solid #ddd; padding: 8px; vertical-align: top;"}
             day2-session]]))]])))

^:kindly/hide-code
(columnar-schedule-table (:day1 schedule-data) (:day2 schedule-data))

^:kindly/hide-code
(kind/hiccup
 [:div
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
      
      // Get date headers (2nd and 3rd columns)
      const headers = table.querySelectorAll('th');
      const day1Header = headers[1];
      const day2Header = headers[2];
      
      // Extract dates from headers
      const processDateHeader = (header) => {
        if (!header) return null;
        const dateText = header.textContent.trim();
        const dateMatch = dateText.match(/(\\w+), (\\w+) (\\d+), (\\d+)/);
        if (dateMatch) {
          const [, , month, day, year] = dateMatch;
          const monthNames = ['January','February','March','April','May','June',
                             'July','August','September','October','November','December'];
          const monthNum = monthNames.indexOf(month);
          return { month: monthNum, day: parseInt(day), year: parseInt(year) };
        }
        return null;
      };
      
      const day1Date = processDateHeader(day1Header);
      const day2Date = processDateHeader(day2Header);
      
      if (day1Date && day2Date) {
        const rows = table.querySelectorAll('tbody tr');
        rows.forEach(row => {
          const cells = row.querySelectorAll('td');
          const timeCell = cells[0];
          
          if (timeCell && timeCell.textContent.match(/\\d{2}:\\d{2}-\\d{2}:\\d{2}/)) {
            const timeText = timeCell.textContent.trim();
            const [startTime, endTime] = timeText.split('-');
            const [startHour] = startTime.split(':');
            const [endHour] = endTime.split(':');
            
            // Convert for day 1 (using day1Date)
            const startDate1 = new Date(Date.UTC(day1Date.year, day1Date.month, day1Date.day, parseInt(startHour), 0));
            const endDate1 = new Date(Date.UTC(day1Date.year, day1Date.month, day1Date.day, parseInt(endHour), 0));
            
            const localStartTime = startDate1.toLocaleTimeString('en-US', {
              hour: '2-digit',
              minute: '2-digit',
              hour12: false,
              timeZone: userTimezone
            });
            
            const localEndTime = endDate1.toLocaleTimeString('en-US', {
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
        });
      }
    }
  });
});
"])
