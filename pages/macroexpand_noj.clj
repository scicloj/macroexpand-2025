^:kindly/hide-code
(ns macroexpand-noj
  (:require [scicloj.kindly.v4.kind :as kind]
            [clojure.edn :as edn]
            [clojure.string :as str]))

^:kindly/hide-code
(def conference-info
  (edn/read-string (slurp "info.edn")))

^:kindly/hide-code
(kind/hiccup
 [:div {:style "display: flex; justify-content: flex-end; margin: 1rem 0; padding: 0.5rem; background: #f8f9fa; border-radius: 6px; border: 1px solid #e0e0e0;"}
  [:div {:style "display: flex; align-items: center; gap: 0.5rem;"}
   [:span {:style "font-size: 0.9rem; color: #666;"} "Supported by: "]
   (for [[company-key company-data] (:supporting-companies conference-info)]
     [:a {:key (name company-key)
          :href "./index.html#supporting-companies"
          :style "display: inline-flex; align-items: center; gap: 0.5rem; margin-left: 0.5rem; text-decoration: none;"}
      (when-let [logo-mark (:logo-mark company-data)]
        [:img {:src logo-mark
               :alt (str (:name company-data) " mark")
               :style "height: 20px; width: auto;"}])
      (when-let [logo (:logo company-data)]
        [:img {:src logo
               :alt (str (:name company-data) " logo")
               :style "height: 20px; width: auto;"}])])]])

;; ## Macroexpand-Noj - growing the Noj ecosystem
;; 
;; **📅 October 17-18, 2024 | Online Conference**
;;
;; The Macroexpand-Noj conference focused on the growth of the [Noj](https://scicloj.github.io/noj) toolkit for data science. Noj is a Clojure toolkit that brings together data processing, visualization, and scientific computing libraries into a cohesive whole. This conference was one of the [Macroexpand 2025](./) pair of conferences organized by [Scicloj](https://scicloj.github.io/).
;; 
;; ### About the Conference
;;
;; A two-day online event that brought together the community to share practical data science experiences, showcase Noj ecosystem tools, and advance Clojure's data science capabilities through tutorials, case studies, and technical discussions.
;;
;; 📹 **Recordings are being published on [Scicloj's YouTube channel](https://www.youtube.com/@Scicloj)**

^:kindly/hide-code
(kind/hiccup
 [:div {:style "text-align: center; margin: 2rem 0; padding: 1.5rem; background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%); border-radius: 12px; border: 2px solid #0ea5e9;"}
  [:h3 {:style "margin: 0 0 0.5rem 0; color: #0369a1;"} "Thank you for attending! 🎉"]
  [:p {:style "margin: 0; font-size: 1rem; color: #666;"}
   "Recordings are being published on "
   [:a {:href "https://www.youtube.com/@Scicloj" :target "_blank" :style "color: #0284c7; text-decoration: underline;"} "Scicloj's YouTube channel"] "."]])

;; ### Schedule

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
        [:div {:style "display: flex; align-items: center; justify-content: space-between; gap: 0.5rem;"}
         [:p {:style "display: inline; margin: 0; font-size: 1rem; font-weight: bold; flex: 1; min-width: 0;"}
          session-title]
         (when (seq speaker-images)
           [:div {:style "display: flex; align-items: center; flex-wrap: wrap; gap: 4px; max-width: 200px;"}
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
                [:div {:style "clear: both;"}]]))])

        ;; Video recording
        (when-let [youtube-id (:youtube-id session-data)]
          [:div {:style "margin-top: 1.5rem;"}
           [:h4 "Recording"]
           [:div {:style "position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; max-width: 100%;"}
            [:iframe {:style "position: absolute; top: 0; left: 0; width: 100%; height: 100%;"
                      :src (str "https://www.youtube.com/embed/" youtube-id)
                      :title session-title
                      :frameborder "0"
                      :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                      :allowfullscreen true}]]])]]])))

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
(defn session-type-legend []
  (kind/hiccup
   [:div {:class "session-legend"}
    [:div {:class "legend-item"}
     [:span {:class "legend-icon"} "💬"]
     [:div {:class "legend-color session-type-talk"}]
     [:span {:class "legend-label"} "Talk"]]
    [:div {:class "legend-item"}
     [:span {:class "legend-icon"} "📚"]
     [:div {:class "legend-color session-type-tutorial"}]
     [:span {:class "legend-label"} "Tutorial"]]
    [:div {:class "legend-item"}
     [:span {:class "legend-icon"} "🗣️"]
     [:div {:class "legend-color session-type-discussion"}]
     [:span {:class "legend-label"} "Discussion"]]
    [:div {:class "legend-item"}
     [:span {:class "legend-icon"} "⚙️"]
     [:div {:class "legend-color session-type-practice"}]
     [:span {:class "legend-label"} "Practice"]]
    [:div {:class "legend-item"}
     [:span {:class "legend-icon"} "🤝"]
     [:div {:class "legend-color session-type-community"}]
     [:span {:class "legend-label"} "Community"]]]))

^:kindly/hide-code
(defn analyze-schedule-spans
  "Analyzes a schedule vector and returns info about sessions that span multiple slots.
  Returns a map of {index -> {:session-key key :span count :render? bool}}"
  [schedule-vec]
  (loop [idx 0
         result {}]
    (if (>= idx (count schedule-vec))
      result
      (if (contains? result idx)
        ;; Already processed as part of a span
        (recur (inc idx) result)
        ;; Find consecutive duplicates
        (let [session-key (nth schedule-vec idx)
              consecutive-count (count (take-while
                                        #(= % session-key)
                                        (drop idx schedule-vec)))]
          (if (> consecutive-count 1)
            ;; Multi-slot session - mark first as renderable with span, rest as non-renderable
            (recur (+ idx consecutive-count)
                   (merge result
                          {idx {:session-key session-key
                                :span consecutive-count
                                :render? true}}
                          (into {} (map (fn [i] [i {:session-key session-key
                                                    :span 0
                                                    :render? false}])
                                        (range (inc idx) (+ idx consecutive-count))))))
            ;; Single-slot session
            (recur (inc idx)
                   (assoc result idx {:session-key session-key
                                      :span 1
                                      :render? true}))))))))

^:kindly/hide-code
^:kindly/hide-code
(defn columnar-schedule-table [day1-data day2-data]
  (let [noj-conf (get-in conference-info [:conferences :macroexpand-noj])
        schedule (:schedule noj-conf)
        day1-schedule (:day1 schedule)
        day2-schedule (:day2 schedule)
        day1-spans (analyze-schedule-spans day1-schedule)
        day2-spans (analyze-schedule-spans day2-schedule)
        sessions (:sessions conference-info)

        day1-slots (sort (:slots day1-data))
        day2-slots (sort (:slots day2-data))
        ;; Use all unique time slots from both days, sorted
        all-time-slots (sort (distinct (concat (map first day1-slots) (map first day2-slots))))

        ;; Helper to get session-type CSS class
        get-session-type-class (fn [session-key]
                                 (when session-key
                                   (let [session-type (get-in sessions [session-key :session-type])]
                                     (when session-type
                                       (str "session-type-" (name session-type))))))

        ;; Helper to format time range for multi-slot sessions
        format-time-range (fn [start-time span-count]
                            (if (> span-count 1)
                              (let [[hour _] (str/split start-time #":")
                                    start-hour (Integer/parseInt hour)
                                    end-hour (+ start-hour span-count)]
                                (str start-time " - " (format "%02d:00" end-hour)))
                              start-time))]
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
      [:table {:class "schedule-table"
               :role "table"
               :aria-label "Conference schedule for October 17-18, 2025"}
       [:thead
        [:tr
         [:th {:id "time-header" :scope "col"} "Time"]
         [:th {:scope "col"} (:date day1-data)]
         [:th {:scope "col"} (:date day2-data)]]]
       [:tbody
        (for [[idx time-slot] (map-indexed vector all-time-slots)]
          (let [day1-span-info (get day1-spans idx)
                day2-span-info (get day2-spans idx)
                day1-session (get (into {} day1-slots) time-slot)
                day2-session (get (into {} day2-slots) time-slot)
                day1-session-key (:session-key day1-span-info)
                day2-session-key (:session-key day2-span-info)
                day1-type-class (get-session-type-class day1-session-key)
                day2-type-class (get-session-type-class day2-session-key)]
            [:tr
             [:td {:class "time-cell"} time-slot]
             (when (:render? day1-span-info true)
               [:td (merge {:class (str "session-cell " day1-type-class)}
                           (when (> (:span day1-span-info 1) 1)
                             {:rowspan (:span day1-span-info 1)}))
                day1-session])
             (when (:render? day2-span-info true)
               [:td (merge {:class (str "session-cell " day2-type-class)}
                           (when (> (:span day2-span-info 1) 1)
                             {:rowspan (:span day2-span-info 1)}))
                day2-session])]))]]

      ;; Mobile stacked layout - now respects spans and has color classes
      [:div {:class "mobile-schedule"}
       [:div {:class "mobile-day-section"}
        [:h3 (:date day1-data)]
        (for [[idx time-slot] (map-indexed vector all-time-slots)]
          (let [day1-span-info (get day1-spans idx)
                day1-session (get (into {} day1-slots) time-slot)
                day1-session-key (:session-key day1-span-info)
                day1-type-class (get-session-type-class day1-session-key)]
            (when (:render? day1-span-info true)
              [:div {:class "mobile-time-slot"}
               [:div {:class "mobile-time-header"}
                (format-time-range time-slot (:span day1-span-info 1))]
               [:div {:class (str "mobile-session-content " day1-type-class)} day1-session]])))]

       [:div {:class "mobile-day-section"}
        [:h3 (:date day2-data)]
        (for [[idx time-slot] (map-indexed vector all-time-slots)]
          (let [day2-span-info (get day2-spans idx)
                day2-session (get (into {} day2-slots) time-slot)
                day2-session-key (:session-key day2-span-info)
                day2-type-class (get-session-type-class day2-session-key)]
            (when (:render? day2-span-info true)
              [:div {:class "mobile-time-slot"}
               [:div {:class "mobile-time-header"}
                (format-time-range time-slot (:span day2-span-info 1))]
               [:div {:class (str "mobile-session-content " day2-type-class)} day2-session]])))]]])))

^:kindly/hide-code
(session-type-legend)

^:kindly/hide-code
(columnar-schedule-table (:day1 schedule-data) (:day2 schedule-data))

^:kindly/hide-code
(kind/hiccup
 [:div
  [:p {:id "timezone-notice" :style "margin-top: 1rem; color: #666; font-size: 0.9rem;"}
   [:em "Detecting your timezone..."]]])

;; ### Continue the Conversation
;;
;; The [#macroexpand-2025](https://clojurians.zulipchat.com/#narrow/channel/536233-macroexpand-2025/) Zulip channel remains active! Join the ongoing discussion about data science in Clojure, ask questions, and connect with the community.

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
    timezoneNotice.setAttribute('aria-live', 'polite');
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
      // Function to convert time text
      const convertTime = (timeText, date) => {
        const [startHour, startMinute] = timeText.split(':');
        const startDate = new Date(Date.UTC(date.year, date.month, date.day, parseInt(startHour), parseInt(startMinute)));
        return startDate.toLocaleTimeString('en-US', {
          hour: '2-digit',
          minute: '2-digit',
          hour12: false,
          timeZone: userTimezone
        });
      };
      
      // Convert desktop table times
      const rows = table.querySelectorAll('tbody tr');
      rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        const timeCell = cells[0];
        
        if (timeCell && timeCell.textContent.match(/\\d{2}:\\d{2}/)) {
          const timeText = timeCell.textContent.trim();
          const localStartTime = convertTime(timeText, day1Date);
          
          // Store UTC time as tooltip
          timeCell.title = 'UTC: ' + timeText;
          timeCell.style.cursor = 'help';
          timeCell.textContent = localStartTime;
        }
      });
      
      // Convert mobile layout times
      const mobileTimeHeaders = document.querySelectorAll('.mobile-time-header');
      mobileTimeHeaders.forEach(timeHeader => {
        if (timeHeader.textContent.match(/\\d{2}:\\d{2}/)) {
          const timeText = timeHeader.textContent.trim();
          const localStartTime = convertTime(timeText, day1Date);
          
          // Store UTC time as tooltip
          timeHeader.title = 'UTC: ' + timeText;
          timeHeader.style.cursor = 'help';
          timeHeader.textContent = localStartTime;
        }
      });
    }
  });
});
"])
