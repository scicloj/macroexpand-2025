^:kindly/hide-code
(ns people
  "Conference people page - speakers, panelists, organizers"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-data
  (edn/read-string (slurp "info.edn")))

;; # People

;; Meet the amazing people behind Macroexpand 2025! Our conferences bring together speakers, organizers, and community members who are passionate about growing Clojure in data science and AI.

^:kindly/hide-code
(defn person-card [person-key person-data roles sessions]
  (let [person-name (:full-name person-data)
        bio (:bio person-data)
        image (or (when-let [images (:images person-data)]
                    (str "images/" (first images)))
                  "images/silhouette.svg")
        role-text (str/join " • " roles)
        session-items (mapv (fn [[session-id session-data]]
                              (let [session-anchor (str/replace (name session-id) #"^session/" "")]
                                [:li {:style "margin-bottom: 0.25rem;"}
                                 [:a {:href (str "sessions.html#" session-anchor)
                                      :style "color: #0066cc; text-decoration: none;"}
                                  (:title session-data)]]))
                            sessions)]
    (kind/hiccup
     [:div {:style "margin-bottom: 2rem; padding: 1.5rem; border: 1px solid #eee; border-radius: 8px; background: #fafafa;"}
      [:div {:style "display: flex; gap: 1.5rem; align-items: flex-start;"}
       [:img {:src image
              :alt person-name
              :style "width: 120px; height: 120px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"}]
       [:div {:style "flex: 1;"}
        [:h3 {:style "margin: 0 0 0.5rem 0; color: #333;"} person-name]
        [:p {:style "margin: 0 0 1rem 0; font-weight: bold; color: #666; font-size: 0.9rem;"} role-text]
        [:p {:style "margin: 0; line-height: 1.6;"} bio]
        (when (seq sessions)
          [:div {:style "margin-top: 1rem;"}
           [:p {:style "margin: 0 0 0.5rem 0; font-weight: bold; color: #555; font-size: 0.9rem;"} "Sessions:"]
           (into [:ul {:style "margin: 0; padding-left: 1.5rem;"}] session-items)])]]])))

^:kindly/hide-code
(defn determine-roles [person-key conference-data]
  (let [person-data (get-in conference-data [:people person-key])
        person-roles (set (:roles person-data))
        roles []
        ;; Check for explicit roles first
        roles (cond-> roles
                (contains? person-roles :organizer) (conj "Organizer")
                (contains? person-roles :host) (conj "Host"))
        ;; Check if in hosts list (backwards compatibility)
        roles (if (and (empty? roles)
                       (contains? (set (:hosts conference-data)) person-key))
                (conj roles "Conference Organizer")
                roles)
        ;; Check if Noj speaker
        noj-sessions (->> (:sessions conference-data)
                          (filter #(= :macroexpand-noj (:conference (second %))))
                          (filter #(contains? (set (:speakers (second %))) person-key)))
        roles (if (seq noj-sessions)
                (conj roles "Macroexpand-Noj Speaker")
                roles)
        ;; Check if Deep speaker
        deep-sessions (->> (:sessions conference-data)
                           (filter #(= :macroexpand-deep (:conference (second %))))
                           (filter #(contains? (set (:speakers (second %))) person-key)))
        roles (if (seq deep-sessions)
                (conj roles "Macroexpand-Deep Speaker")
                roles)
        ;; Check if other speaker
        other-sessions (->> (:sessions conference-data)
                            (filter #(nil? (:conference (second %))))
                            (filter #(contains? (set (:speakers (second %))) person-key)))
        roles (if (seq other-sessions)
                (conj roles "Speaker")
                roles)
        ;; Collect all sessions
        all-sessions (concat noj-sessions deep-sessions other-sessions)]
    {:roles roles
     :sessions all-sessions}))

^:kindly/hide-code
(def all-people-with-roles
  (->> (:people conference-data)
       (map (fn [[person-key person-data]]
              (let [role-data (determine-roles person-key conference-data)]
                [person-key person-data (:roles role-data) (:sessions role-data)])))
       (filter #(seq (nth % 2))) ; Only include people with roles
       (sort-by #(:full-name (second %)))))

^:kindly/hide-code
(def speakers-only
  (filter (fn [[person-key person-data roles sessions]]
            (not (some #{"Organizer" "Host"} roles)))
          all-people-with-roles))

^:kindly/hide-code
(def organizers
  (filter (fn [[person-key person-data roles sessions]]
            (some #{"Organizer" "Host"} roles))
          all-people-with-roles)) ; Sort alphabetically by name

^:kindly/hide-code
(kind/fragment
 (for [[person-key person-data roles sessions] speakers-only]
   (person-card person-key person-data roles sessions)))

;; ## Organizers

^:kindly/hide-code
(kind/fragment
 (for [[person-key person-data roles sessions] organizers]
   (person-card person-key person-data roles sessions)))

;; ---

;; *Learn more about our [sessions](sessions.html) and the [conferences](index.html).*
