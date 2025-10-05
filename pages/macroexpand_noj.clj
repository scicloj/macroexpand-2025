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
;; 📌 **[How to participate, register, and join →](./#how-to-participate)**
;;
;; ### Tentative Schedule

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
^:kindly/hide-code
(defn session-card-new [session-key session-data people-data]
  (let [session-title (:title session-data)
        speakers (:speakers session-data)
        speaker-names (format-speaker-list speakers people-data)
        abstract (:abstract session-data)
        speaker-images (when (seq speakers)
                         (for [speaker-key speakers]
                           (let [speaker-data (get people-data speaker-key)
                                 speaker-image (or (some-> speaker-data :images first)
                                                   "silhouette.svg")]
                             [:img {:src (str "images/" speaker-image)
                                    :alt (:full-name speaker-data)
                                    :style "width: 60px; height: 60px; border-radius: 50%; object-fit: cover; margin-right: 8px;"}])))]
    (kind/hiccup
     [:div {:style "margin-bottom: 1.5rem; border-left: 3px solid #ddd; padding-left: 1rem;"}

      [:details
       [:summary {:style "cursor: pointer; list-style: none; padding: 0.5rem 0;"}
        [:div {:style "display: flex; align-items: center; justify-content: space-between;"}
         [:p {:style "display: inline; margin: 0; font-size: 1rem; font-weight: bold;"}
          session-title]
         (when (seq speaker-images)
           [:div {:style "display: flex; align-items: center; flex-shrink: 0;"}
            speaker-images])]]

       ;; Expanded content
       [:div {:style "padding: 1rem 0;"}
        ;; Abstract section
        [:div {:style "margin-bottom: 1.5rem;"}
         [:h4 "Abstract"]
         (kind/md abstract)]

        ;; Speaker details with images
        (when (seq speakers)
          [:div
           [:h4 (if (> (count speakers) 1) "Speakers" "Speaker")]
           (for [speaker-key speakers]
             (let [speaker-data (get people-data speaker-key)
                   speaker-image-src (or (some-> speaker-data :images first)
                                         "silhouette.svg")]
               [:div {:key speaker-key :style "margin-bottom: 1.5rem; overflow: hidden;"}
                [:img {:src (str "images/" speaker-image-src)
                       :alt (:full-name speaker-data)
                       :style "width: 80px; height: 80px; border-radius: 50%; object-fit: cover; margin-right: 1rem; float: left;"}]
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
               [(format "%02d:00" hour)
                (session-key->display session-key sessions-data people-data)]))
           schedule-vec))))

^:kindly/hide-code
(defn date-string->day-name
  "Convert date string like '2025-10-17' to 'Friday, October 17'"
  [date-str]
  (let [[year month day] (str/split date-str #"-")
        months ["January" "February" "March" "April" "May" "June"
                "July" "August" "September" "October" "November" "December"]
        month-name (nth months (dec (Integer/parseInt month)))
        day-num (Integer/parseInt day)]
    ;; Correct days: Oct 17, 2025 = Friday, Oct 18, 2025 = Saturday
    (if (= date-str "2025-10-17")
      (str "Friday, " month-name " " day-num)
      (str "Saturday, " month-name " " day-num))))

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
     [:div
      ;; Ultra-aggressive CSS to override any external table styles
      [:style "
        .schedule-container {
          width: 100% !important;
        }
        table.schedule-table {
          width: 100% !important; 
          border-collapse: collapse !important; 
          margin: 1rem 0 !important;
          table-layout: fixed !important;
        }
        /* Override Quarto's automatic colgroup */
        table.schedule-table colgroup col:first-child {
          width: 120px !important;
        }
        table.schedule-table colgroup col:nth-child(2),
        table.schedule-table colgroup col:nth-child(3) {
          width: calc((100% - 120px) / 2) !important;
        }
        table.schedule-table th,
        table.schedule-table td {
          border: 1px solid #ddd !important; 
          padding: 8px !important; 
          vertical-align: top !important;
          box-sizing: border-box !important;
        }
        table.schedule-table th {
          background-color: #f8f9fa !important; 
          text-align: left !important; 
          font-weight: bold !important;
        }
        table.schedule-table th:first-child {
          width: 120px !important; 
          max-width: 120px !important;
          min-width: 120px !important;
          text-align: center !important;
          padding: 6px !important;
          word-wrap: break-word !important;
          hyphens: auto !important;
        }
        table.schedule-table td:first-child { 
          width: 120px !important; 
          max-width: 120px !important;
          min-width: 120px !important;
          text-align: center !important;
          white-space: nowrap !important;
          overflow: hidden !important;
          padding: 6px !important;
        }
        table.schedule-table th:nth-child(2),
        table.schedule-table th:nth-child(3),
        table.schedule-table td:nth-child(2),
        table.schedule-table td:nth-child(3) { 
          width: calc((100% - 120px) / 2) !important;
        }
        table.schedule-table .time-cell {
          font-family: monospace !important; 
          background-color: #f8f9fa !important;
          text-align: center !important;
          font-size: 0.75rem !important;
        }
        table.schedule-table .session-cell {
          padding: 8px !important;
        }
        
        /* Mobile responsive styles */
        @media (max-width: 768px) {
          table.schedule-table {
            display: none !important;
          }
          .mobile-schedule {
            display: block !important;
          }
          .mobile-day-section {
            margin-bottom: 2rem;
          }
          .mobile-day-section h3 {
            background-color: #f8f9fa;
            padding: 1rem;
            margin: 0 0 1rem 0;
            border: 1px solid #ddd;
            font-size: 1.1rem;
          }
          .mobile-time-slot {
            border: 1px solid #ddd;
            margin-bottom: 0.5rem;
          }
          .mobile-time-header {
            background-color: #f8f9fa;
            padding: 8px 12px;
            font-family: monospace;
            font-weight: bold;
            border-bottom: 1px solid #ddd;
          }
          .mobile-session-content {
            padding: 8px;
          }
        }
        
        /* Hide mobile layout on desktop */
        @media (min-width: 769px) {
          .mobile-schedule {
            display: none !important;
          }
        }
      "]

      ;; Desktop table layout with ultra-specific selectors
      [:table {:class "schedule-table"}
       [:thead
        [:tr
         [:th {:id "time-header"} "Time"]
         [:th (:date day1-data)]
         [:th (:date day2-data)]]]
       [:tbody
        (for [time-slot all-time-slots]
          (let [day1-session (get (into {} day1-slots) time-slot)
                day2-session (get (into {} day2-slots) time-slot)]
            [:tr
             [:td {:class "time-cell"} time-slot]
             [:td {:class "session-cell"} day1-session]
             [:td {:class "session-cell"} day2-session]]))]]

      ;; Mobile stacked layout
      [:div {:class "mobile-schedule"}
       [:div {:class "mobile-day-section"}
        [:h3 (:date day1-data)]
        (for [time-slot all-time-slots]
          (let [day1-session (get (into {} day1-slots) time-slot)]
            [:div {:class "mobile-time-slot"}
             [:div {:class "mobile-time-header"} time-slot]
             [:div {:class "mobile-session-content"} day1-session]]))]

       [:div {:class "mobile-day-section"}
        [:h3 (:date day2-data)]
        (for [time-slot all-time-slots]
          (let [day2-session (get (into {} day2-slots) time-slot)]
            [:div {:class "mobile-time-slot"}
             [:div {:class "mobile-time-header"} time-slot]
             [:div {:class "mobile-session-content"} day2-session]]))]]])))

^:kindly/hide-code
(columnar-schedule-table (:day1 schedule-data) (:day2 schedule-data))

^:kindly/hide-code
(kind/hiccup
 [:div
  [:p {:id "timezone-notice" :style "margin-top: 1rem; color: #666; font-size: 0.9rem;"}
   [:em "Detecting your timezone..."]]

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
  
  // Update time header to show user's timezone (abbreviated)
  const timeHeader = document.getElementById('time-header');
  if (timeHeader) {
    // Shorten timezone for compact display
    const shortTz = userTimezone.split('/').pop() || userTimezone;
    timeHeader.textContent = 'Time - ' + shortTz;
    timeHeader.title = 'Times shown in: ' + userTimezone;
  }
  
  // Update timezone notice
  const timezoneNotice = document.getElementById('timezone-notice');
  if (timezoneNotice) {
    timezoneNotice.innerHTML = '<em>Times shown in your local timezone: ' + userTimezone + '</em>';
  }
  
  // Find all tables and process them
  const tables = document.querySelectorAll('table');
  
  tables.forEach(table => {
    // Get date headers (2nd and 3rd columns)
    const headers = table.querySelectorAll('th');
    const day1Header = headers[1];
    const day2Header = headers[2];
      
    // Extract dates from headers
    const processDateHeader = (header) => {
      if (!header) return null;
      const dateText = header.textContent.trim();
      // Updated regex to match \"Friday, October 17\" format (no year)
      const dateMatch = dateText.match(/(\\w+), (\\w+) (\\d+)/);
      if (dateMatch) {
        const [, , month, day] = dateMatch;
        const monthNames = ['January','February','March','April','May','June',
                           'July','August','September','October','November','December'];
        const monthNum = monthNames.indexOf(month);
        return { month: monthNum, day: parseInt(day), year: 2025 }; // Hardcode 2025
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
        
        if (timeCell && timeCell.textContent.match(/\\d{2}:\\d{2}/)) {
          const timeText = timeCell.textContent.trim();
          const [startHour, startMinute] = timeText.split(':');
          
          // Convert for day 1 (using day1Date) 
          const startDate1 = new Date(Date.UTC(day1Date.year, day1Date.month, day1Date.day, parseInt(startHour), parseInt(startMinute)));
          
          const localStartTime = startDate1.toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false,
            timeZone: userTimezone
          });
          
          // Store UTC time as tooltip
          timeCell.title = 'UTC: ' + timeText;
          timeCell.style.cursor = 'help';
          
          timeCell.textContent = localStartTime;
        }
      });
    }
  });
});
"])
