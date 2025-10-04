(ns qmd-generator
  "Utilities for generating Quarto markdown files from structured conference data"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [scicloj.clay.v2.api :as clay]))

(defn load-conference-data
  "Load conference data from info.edn"
  []
  (edn/read-string (slurp "info.edn")))

(defn sanitize-filename
  "Convert a title to a safe filename"
  [title]
  (-> title
      str/lower-case
      (str/replace #"[^\w\s-]" "")
      (str/replace #"\s+" "-")
      (str/replace #"-+" "-")
      (str/trim)))

(defn convert-image-path
  "Convert image path from data/speaker-images/ to images/speakers/"
  [image-path]
  (when image-path
    (if (.startsWith image-path "data/speaker-images/")
      (str "images/speakers/" (.substring image-path 20))
      image-path)))

(defn format-speaker-list
  "Format speakers for display"
  [speaker-keys people-data]
  (->> speaker-keys
       (map #(get-in people-data [% :full-name]))
       (str/join ", ")))

(defn generate-session-frontmatter
  "Generate Quarto frontmatter for a session"
  [session-title session-data people-data]
  (let [conference-name (case (:conference session-data)
                          :macroexpand-noj "Macroexpand-Noj"
                          :macroexpand-deep "Macroexpand-Deep"
                          "Macroexpand 2025")]
    (str "---\n"
         "title: \"" session-title "\"\n"
         "subtitle: \"" conference-name "\"\n"
         "format: html\n"
         "---\n\n")))

(defn generate-session-content
  "Generate the main content for a session page"
  [session-title session-data people-data]
  (let [speakers (:speakers session-data)]
    (str
     ;; Abstract section
     "## Abstract\n\n"
     (:abstract session-data) "\n\n"

     ;; Speaker bios section
     (when (seq speakers)
       (str "## Speaker" (when (> (count speakers) 1) "s") "\n\n"
            (->> speakers
                 (map (fn [speaker-key]
                        (let [speaker-data (get people-data speaker-key)]
                          (str "### " (:full-name speaker-data) "\n\n"
                               (when-let [images (:images speaker-data)]
                                 (str "![" (:full-name speaker-data) "](" (convert-image-path (first images)) "){width=150}\n\n"))
                               (:bio speaker-data) "\n\n"))))
                 (str/join))))

     ;; Footer
     "---\n\n"
     "*This session is part of [Macroexpand 2025](index.html) - Clojure through Data and AI*\n")))

(defn generate-session-qmd
  "Generate a complete QMD file for a session"
  [session-title session-data people-data]
  (str (generate-session-frontmatter session-title session-data people-data)
       (generate-session-content session-title session-data people-data)))

(defn write-session-file
  "Write a session QMD file to the site directory"
  [session-title session-data people-data]
  (let [filename (str "site/session-" (sanitize-filename session-title) ".qmd")
        content (generate-session-qmd session-title session-data people-data)]
    (io/make-parents filename)
    (spit filename content)
    (println "Generated:" filename)))

(defn copy-speaker-images
  "Copy speaker images to site directory"
  []
  (let [source-dir "data/speaker-images"
        target-dir "site/images/speakers"]
    (io/make-parents (str target-dir "/dummy"))
    (when (.exists (io/file source-dir))
      (doseq [file (filter #(.isFile %) (file-seq (io/file source-dir)))
              :when (not= (.getName file) "README.md")]
        (io/copy file (io/file target-dir (.getName file))))
      (println "Copied speaker images to" target-dir))))

(defn generate-all-session-files
  "Generate QMD files for all sessions"
  []
  (let [{:keys [sessions people]} (load-conference-data)]
    (println "Copying speaker images...")
    (copy-speaker-images)
    (println "Generating session files...")
    (doseq [[session-title session-data] sessions]
      (write-session-file session-title session-data people))
    (println "Generated" (count sessions) "session files")))

(defn -main
  "Main entry point for generating session files"
  [& args]
  (generate-all-session-files)
  (clay/make! {:aliases [:markdown]})
  (System/exit 0))

(comment
  (-main))
