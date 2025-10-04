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

;; ## Conference Organizers

^:kindly/hide-code
(defn person-card [person-key person-data roles]
  (let [name (:full-name person-data)
        bio (:bio person-data)
        image (when-let [images (:images person-data)]
                (str "images/" (first images)))
        role-text (str/join " • " roles)]
    (kind/hiccup
     [:div {:style "margin-bottom: 2rem; padding: 1.5rem; border: 1px solid #eee; border-radius: 8px; background: #fafafa;"}
      [:div {:style "display: flex; gap: 1.5rem; align-items: flex-start;"}
       (when image
         [:img {:src image
                :alt name
                :style "width: 120px; height: 120px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"}])
       [:div {:style "flex: 1;"}
        [:h3 {:style "margin: 0 0 0.5rem 0; color: #333;"} name]
        [:p {:style "margin: 0 0 1rem 0; font-weight: bold; color: #666; font-size: 0.9rem;"} role-text]
        [:p {:style "margin: 0; line-height: 1.6;"} bio]]]])))

^:kindly/hide-code
(defn determine-roles [person-key conference-data]
  (let [roles []
        ;; Check if organizer
        roles (if (contains? (set (:hosts conference-data)) person-key)
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
                roles)]
    roles))

^:kindly/hide-code
(def all-people-with-roles
  (->> (:people conference-data)
       (map (fn [[person-key person-data]]
              [person-key person-data (determine-roles person-key conference-data)]))
       (filter #(seq (nth % 2))) ; Only include people with roles
       (sort-by #(:full-name (second %))))) ; Sort alphabetically by name

^:kindly/hide-code
(kind/fragment
 (for [[person-key person-data roles] all-people-with-roles]
   (person-card person-key person-data roles)))

;; ---

;; *Learn more about our [sessions](sessions.html) and the [conferences](index.html).*
