^:kindly/hide-code
(ns speakers
  (:require [clojure.edn :as edn]
            [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]))

^:kindly/hide-code
(def conference-info (edn/read-string (slurp "info.edn")))

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

;; ## Speakers
;; 
;; At the Macroexpand conferences, we focused on talks and projects that help Clojure grow.
;; 
;; Our guidelines, based on experience, ensured that work on each talk was meaningful and fruitful. All talks covered active projects, ranging from short-term experiments to long-term reports, from analyses to documentation or library improvements. Good talks combined storytelling with actual progress on projects.
;; 
;; ### Speaker Guidelines
;; 
;; The conferences followed these guidelines:
;; 
;; - Speakers worked with organizers early to shape project ideas
;; - All talks included clear notes and reproducible code, hosted on [Clojure Civitas](https://clojurecivitas.github.io/) or [Clojure Data Tutorials](https://scicloj.github.io/clojure-data-tutorials/)
;; - Notes were fully reproducible, including all data and clear usage instructions
;; - Talks were designed for general Clojure programmers, with accessibility for newcomers
;; - Each conference ([Macroexpand-Noj](./macroexpand_noj.html) & [Macroexpand-Deep](./macroexpand_deep.html)) had a list of preferred topics that were prioritized
;; - Team presentations and cross-field collaborations were encouraged
;; 
;; ### Thank You to Our Speakers
;; 
;; We're grateful to all the speakers who shared their work and insights with the community. Your contributions made these conferences a success!
;; 
;; See the full list of sessions and speakers on the [Sessions page](./sessions.html), and check out the conference schedules:
;; - [Macroexpand-Noj Schedule](./macroexpand_noj.html) (October 17-18, 2024)
;; - [Macroexpand-Deep Schedule](./macroexpand_deep.html) (October 24-25, 2024)
;; 
;; 📹 **Recordings are being published on [Scicloj's YouTube channel](https://www.youtube.com/@Scicloj)**
