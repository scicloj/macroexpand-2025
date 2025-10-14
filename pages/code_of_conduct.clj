^:kindly/hide-code
(ns code-of-conduct
  "Conference Code of Conduct page"
  (:require [clojure.edn :as edn]
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

;; # Code of Conduct

;; Macroexpand is dedicated to providing a welcoming, inclusive experience for everyone.

;; We expect all participants to be respectful and considerate in all interactions, welcome newcomers, and support learning.

;; We do not tolerate harassment, discriminatory behavior, or behavior that interrupts sessions or makes others uncomfortable.

;; If you experience or witness unacceptable behavior, please contact the [organizers](./people.html#organizers) immediately by a Direct Message at the [Zulip chat](https://scicloj.github.io/docs/community/chat/). Participants who violate these guidelines may be asked to leave the conference.

;; By participating, you agree to follow this code of conduct.
