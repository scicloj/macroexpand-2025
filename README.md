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

The site is published using the `./docs` directory at the project root. To render the site for publishing, run:

```bash
cd site && quarto render
```
