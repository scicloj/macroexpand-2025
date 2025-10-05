^:kindly/hide-code
(ns sessions
  "Conference sessions overview page"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-data
  (edn/read-string (slurp "info.edn")))

^:kindly/hide-code
;; sanitize-filename function removed - no longer needed for individual session pages

^:kindly/hide-code
(defn format-speaker-list
  "Format speakers for display"
  [speaker-keys people-data]
  (->> speaker-keys
       (map #(get-in people-data [% :full-name]))
       (str/join ", ")))

;; # Sessions

;; Welcome to the Macroexpand 2025 sessions! This year we have two focused conferences:

;; - **[Macroexpand-Noj](macroexpand-noj.html)** (October 17-18) - Data Science with the Noj toolkit
;; - **[Macroexpand-Deep](macroexpand-deep.html)** (October 24-25) - The first Clojure AI conference

;; ## All Sessions

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
         [:h3 {:style "display: inline; margin: 0; font-size: 1.25rem;"}
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
(def session-card session-card-new)

^:kindly/hide-code
(def sessions-by-conference
  (group-by #(:conference (second %)) (:sessions conference-data)))

;; ### Macroexpand-Noj Sessions 🔬

;; *Data Science with the Noj toolkit • October 17-18, 2025*

^:kindly/hide-code
(def noj-sessions (get sessions-by-conference :macroexpand-noj []))

^:kindly/hide-code
(kind/fragment
 (->> noj-sessions
      (map (fn [[session-key data]]
             (session-card session-key data (:people conference-data))))))

;; ### Macroexpand-Deep Sessions 🤖

;; *The first Clojure AI conference • October 24-25, 2025*

^:kindly/hide-code
(def deep-sessions (get sessions-by-conference :macroexpand-deep []))

^:kindly/hide-code
(kind/fragment
 (->> deep-sessions
      (map (fn [[session-key data]]
             (session-card session-key data (:people conference-data))))))

#_(def other-sessions (get sessions-by-conference nil []))

#_(when (seq other-sessions)
    (kind/fragment
     (->> other-sessions
          (map (fn [[title data]]
                 (session-card title data (:people conference-data)))))))

;; ---

;; *Learn more about [Macroexpand 2025](index.html) and our [speakers](speakers.html).*
