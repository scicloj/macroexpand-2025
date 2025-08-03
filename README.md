# macroexpand-2025

This is the website project for the upcoming Macroexpand-2025 conference.

It is developed as a [Quarto Website](https://quarto.org/docs/websites/).
At the moment, all the relevant content is under the [site](./site) directory.

Later on, when we have data about speakers, schedule, sessions, etc., we will turn it into a data-driven Clojure project as we did with the previous conference.

## Development
Make sure you have [Quarto](https://quarto.org/docs/get-started/) installed before starting.
To enable hot-reloading during development, run the following command at the project root:

```bash
cd site && quarto preview
```

## Publishing

The site is automatically published using a GitHub Actions workflow(`.github/workflows/publish.yml`).
Whenever a new commit is pushed to the main branch, the workflow builds the updated site and deploys it to the `gh-pages` branch.
The live site is available at: https://scicloj.github.io/macroexpand-2025/
