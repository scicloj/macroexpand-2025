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
(kind/hiccup
 [:div {:style "display: flex; justify-content: flex-end; margin: 1rem 0; padding: 0.5rem; background: #f8f9fa; border-radius: 6px; border: 1px solid #e0e0e0;"}
  [:div {:style "display: flex; align-items: center; gap: 0.5rem;"}
   [:span {:style "font-size: 0.9rem; color: #666;"} "Supported by: "]
   (for [[company-key company-data] (:supporting-companies conference-data)]
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

;; - **[Macroexpand-Noj](macroexpand_noj.html)** (October 17-18) - Data Science with the Noj toolkit
;; - **[Macroexpand-Deep](macroexpand_deep.html)** (October 24-25) - The first Clojure AI conference

;; ## All Sessions

^:kindly/hide-code
^:kindly/hide-code
(defn session-card-new [session-key session-data people-data]
  (let [session-title (:title session-data)
        speakers (:speakers session-data)
        speaker-names (format-speaker-list speakers people-data)
        abstract (:abstract session-data)
        session-id (str/replace (name session-key) #"^session/" "")
        speaker-images (when (seq speakers)
                         (for [speaker-key speakers]
                           (let [speaker-data (get people-data speaker-key)
                                 speaker-image (or (some-> speaker-data :images first)
                                                   "silhouette.svg")]
                             [:img {:src (str "images/" speaker-image)
                                    :alt (:full-name speaker-data)
                                    :style "width: 60px; height: 60px; border-radius: 50%; object-fit: cover; margin-right: 8px;"}])))]
    (kind/hiccup
     [:div {:id session-id
            :class "session-card"
            :style "margin-bottom: 1.5rem; border-left: 3px solid #ddd; padding-left: 1rem; transition: background-color 0.3s ease;"}

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

^:kindly/hide-code
(kind/hiccup
 [:script {:type "text/javascript"}
  "
  function handleSessionAnchor() {
    const hash = window.location.hash.substring(1);
    if (hash) {
      const sessionCard = document.getElementById(hash);
      if (sessionCard) {
        const details = sessionCard.querySelector('details');
        if (details && !details.open) {
          details.open = true;
        }
        setTimeout(() => {
          sessionCard.scrollIntoView({ behavior: 'smooth', block: 'center' });
          sessionCard.style.backgroundColor = '#fff3cd';
          setTimeout(() => {
            sessionCard.style.backgroundColor = '';
          }, 2000);
        }, 100);
      }
    }
  }
  
  // Handle on page load
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', handleSessionAnchor);
  } else {
    handleSessionAnchor();
  }
  
  // Handle hash changes (clicking links on the same page)
  window.addEventListener('hashchange', handleSessionAnchor);
  "])

;; ---

;; *Learn more about [Macroexpand 2025](index.html) and our [speakers](speakers.html).*
