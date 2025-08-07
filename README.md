# macroexpand-2025

This is the website project for the upcoming Macroexpand-2025 conference.

It is developed as a [Quarto Website](https://quarto.org/docs/websites/).
At the moment, all the relevant content is under the [site](./site) directory.

Later on, when we have data about speakers, schedule, sessions, etc., we will turn it into a data-driven Clojure project as we did with the previous conference.

## Development
First, make sure you have [Quarto](https://quarto.org/docs/get-started/) installed before starting if you haven't already.

This project setup references the setup from [Clojure Civitas](https://github.com/ClojureCivitas/clojurecivitas.github.io) 
with its easy setup for Qaurto website using [Clay](https://scicloj.github.io/clay/).

Follow [these instructions](https://github.com/ClojureCivitas/clojurecivitas.github.io?tab=readme-ov-file#preview-a-webpage-optional-recommended) 
from Clojure Civitas' repo to preview the site locally while developing.

If you wish to make changes to the main page,
you can edit the [index.qmd](https://github.com/scicloj/macroexpand-2025/blob/main/site/index.qmd).
For other pages, edit `clj` files under the `pages` directory at the root.

## Publishing

The site is automatically published using a GitHub Actions workflow(`.github/workflows/publish.yml`).
Whenever a new commit is pushed to the main branch, the workflow builds the updated site and deploys it to the `gh-pages` branch.
The live site is available at: https://scicloj.github.io/macroexpand-2025/
