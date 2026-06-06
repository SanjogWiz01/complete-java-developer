# HTML Development Full Course

HTML means HyperText Markup Language. It is the standard markup language used to
describe the structure and meaning of web pages. CSS controls presentation.
JavaScript controls behavior. HTML provides the content and the document
structure that browsers, search engines, screen readers, and other tools read.

## Course Roadmap

1. Web and HTML basics
2. HTML document structure
3. Text content
4. Links and navigation
5. Images and media
6. Semantic page layout
7. Tables
8. Forms
9. Accessibility
10. Metadata and SEO
11. Connecting CSS and JavaScript
12. Project structure
13. Mini projects
14. Final capstone

## 1. Web and HTML Basics

### How a Web Page Works

A normal website request follows this flow:

```text
User enters URL
        |
        v
Browser requests the page
        |
        v
Server sends HTML, CSS, JS, images, and other files
        |
        v
Browser parses HTML and renders the page
```

HTML is not a programming language. It does not contain loops, conditions, or
business logic. HTML is a markup language: it marks content with elements so the
browser understands the role of each part.

### Tools Needed

- A code editor such as VS Code, IntelliJ IDEA, or Notepad++.
- A modern browser such as Chrome, Edge, Firefox, or Safari.
- Browser developer tools for inspecting elements and debugging markup.

### Your First HTML File

Create a file named `index.html`:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My First Page</title>
</head>
<body>
    <h1>Hello HTML</h1>
    <p>This is my first web page.</p>
</body>
</html>
```

Open the file in a browser. You should see a heading and a paragraph.

## 2. HTML Document Structure

Every professional HTML document should include these core parts:

| Part | Purpose |
| --- | --- |
| `<!DOCTYPE html>` | Tells the browser to use modern HTML rules |
| `html` | Root element of the document |
| `lang` | Defines the page language |
| `head` | Contains metadata, links, and page configuration |
| `meta charset` | Defines character encoding |
| `meta viewport` | Helps responsive layout on mobile screens |
| `title` | Text shown in the browser tab |
| `body` | Visible page content |

### Head vs Body

The `head` contains information about the page:

```html
<head>
    <meta charset="UTF-8">
    <meta name="description" content="A beginner HTML learning page">
    <title>HTML Notes</title>
</head>
```

The `body` contains what the user sees:

```html
<body>
    <h1>HTML Notes</h1>
    <p>HTML structures the content of a web page.</p>
</body>
```

### Elements, Tags, and Attributes

An element usually has an opening tag, content, and a closing tag:

```html
<p>This is a paragraph.</p>
```

Some elements use attributes:

```html
<a href="https://developer.mozilla.org/">Read MDN Docs</a>
```

Here, `href` is an attribute and its value is the URL.

Some elements are void elements. They do not have closing tags:

```html
<img src="profile.jpg" alt="Student profile photo">
<br>
<hr>
<input type="text" name="username">
```

## 3. Text Content

HTML provides elements for meaningful text structure.

### Headings

Use headings to create a clear document outline.

```html
<h1>Complete HTML Course</h1>
<h2>Text Elements</h2>
<h3>Headings</h3>
```

Best practices:

- Use only one main `h1` for the page.
- Do not skip heading levels for visual size.
- Use CSS later to change appearance.

### Paragraphs

```html
<p>
    HTML is used to structure content such as headings, paragraphs, links,
    images, tables, forms, and sections.
</p>
```

### Emphasis and Importance

```html
<p>This topic is <strong>important</strong>.</p>
<p>You should <em>practice daily</em>.</p>
```

Use `strong` for importance and `em` for emphasis. Do not use them only for
visual bold or italic styling.

### Lists

Unordered list:

```html
<ul>
    <li>HTML</li>
    <li>CSS</li>
    <li>JavaScript</li>
</ul>
```

Ordered list:

```html
<ol>
    <li>Create the file.</li>
    <li>Write the HTML.</li>
    <li>Open it in a browser.</li>
</ol>
```

Description list:

```html
<dl>
    <dt>HTML</dt>
    <dd>Structures web content.</dd>

    <dt>CSS</dt>
    <dd>Styles web content.</dd>
</dl>
```

### Quotes and Code

```html
<blockquote>
    HTML gives meaning to content before design is added.
</blockquote>

<p>Use the <code>&lt;h1&gt;</code> element for the main page heading.</p>

<pre><code>&lt;p&gt;Preformatted code keeps spacing.&lt;/p&gt;</code></pre>
```

## 4. Links and Navigation

Links connect pages, sections, files, email addresses, phone numbers, and
external resources.

### Basic Link

```html
<a href="about.html">About Me</a>
```

### External Link

```html
<a href="https://developer.mozilla.org/" target="_blank" rel="noopener">
    MDN Web Docs
</a>
```

Use `rel="noopener"` when opening a new tab with `target="_blank"`.

### Page Section Link

```html
<a href="#contact">Go to Contact</a>

<section id="contact">
    <h2>Contact</h2>
    <p>Email me for project work.</p>
</section>
```

### Email and Phone Links

```html
<a href="mailto:student@example.com">Email Me</a>
<a href="tel:+9779800000000">Call Me</a>
```

### Navigation

```html
<nav aria-label="Main navigation">
    <ul>
        <li><a href="index.html">Home</a></li>
        <li><a href="projects.html">Projects</a></li>
        <li><a href="contact.html">Contact</a></li>
    </ul>
</nav>
```

## 5. Images and Media

Images should always include useful `alt` text unless the image is decorative.

### Basic Image

```html
<img src="assets/profile.jpg" alt="Portrait of Sanjog smiling at a desk">
```

Good alt text describes the useful information in the image.

Poor alt text:

```html
<img src="profile.jpg" alt="image">
```

### Figure and Caption

```html
<figure>
    <img src="assets/html-layout.png" alt="Diagram of a semantic HTML page">
    <figcaption>Semantic HTML layout example.</figcaption>
</figure>
```

### Responsive Image Basics

```html
<picture>
    <source srcset="assets/team-large.jpg" media="(min-width: 800px)">
    <img src="assets/team-small.jpg" alt="Development team discussing a web page">
</picture>
```

### Video

```html
<video controls width="640">
    <source src="assets/demo.mp4" type="video/mp4">
    Your browser does not support the video element.
</video>
```

### Audio

```html
<audio controls>
    <source src="assets/lesson.mp3" type="audio/mpeg">
    Your browser does not support the audio element.
</audio>
```

## 6. Semantic Page Layout

Semantic HTML uses elements that describe the purpose of content.

| Element | Use |
| --- | --- |
| `header` | Introductory content for a page or section |
| `nav` | Navigation links |
| `main` | Main unique content of the page |
| `section` | Thematic group of content |
| `article` | Independent piece of content |
| `aside` | Supporting or related content |
| `footer` | Closing information |
| `div` | Generic block container |
| `span` | Generic inline container |

### Semantic Page Example

```html
<body>
    <header>
        <h1>Student Portfolio</h1>
        <nav aria-label="Main navigation">
            <a href="#about">About</a>
            <a href="#projects">Projects</a>
            <a href="#contact">Contact</a>
        </nav>
    </header>

    <main>
        <section id="about">
            <h2>About</h2>
            <p>I am learning web development and Java programming.</p>
        </section>

        <section id="projects">
            <h2>Projects</h2>
            <article>
                <h3>Cab Booking System</h3>
                <p>A Spring Boot project for booking and managing cab rides.</p>
            </article>
        </section>
    </main>

    <footer>
        <p>&copy; 2026 Student Portfolio</p>
    </footer>
</body>
```

Use `div` only when no semantic element fits.

## 7. Tables

Tables are for tabular data, not for page layout.

### Basic Table

```html
<table>
    <caption>Weekly Study Plan</caption>
    <thead>
        <tr>
            <th scope="col">Day</th>
            <th scope="col">Topic</th>
            <th scope="col">Hours</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <th scope="row">Monday</th>
            <td>HTML Basics</td>
            <td>2</td>
        </tr>
        <tr>
            <th scope="row">Tuesday</th>
            <td>Forms</td>
            <td>2</td>
        </tr>
    </tbody>
</table>
```

Best practices:

- Add a `caption` when the table needs a title.
- Use `thead`, `tbody`, and `tfoot` for structure.
- Use `th` for headers.
- Use `scope` to connect headers with rows or columns.

## 8. Forms

Forms collect user input.

### Basic Form

```html
<form action="/register" method="post">
    <label for="full-name">Full Name</label>
    <input type="text" id="full-name" name="fullName" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" required>

    <button type="submit">Register</button>
</form>
```

Important form rules:

- Every input should have a connected `label`.
- The `for` value on `label` should match the input `id`.
- The `name` attribute is used when form data is submitted.
- Use the correct input type for better validation and mobile keyboards.

### Common Input Types

```html
<input type="text" name="fullName">
<input type="email" name="email">
<input type="password" name="password">
<input type="number" name="age">
<input type="date" name="birthDate">
<input type="time" name="meetingTime">
<input type="file" name="resume">
<input type="checkbox" name="terms">
<input type="radio" name="plan" value="basic">
<input type="range" name="rating" min="1" max="10">
```

### Select, Textarea, and Fieldset

```html
<form action="/contact" method="post">
    <fieldset>
        <legend>Contact Details</legend>

        <label for="topic">Topic</label>
        <select id="topic" name="topic">
            <option value="">Choose a topic</option>
            <option value="project">Project Work</option>
            <option value="support">Support</option>
        </select>

        <label for="message">Message</label>
        <textarea id="message" name="message" rows="5"></textarea>
    </fieldset>

    <button type="submit">Send Message</button>
</form>
```

### Client-Side Validation

```html
<label for="username">Username</label>
<input
    type="text"
    id="username"
    name="username"
    minlength="3"
    maxlength="20"
    pattern="[A-Za-z0-9]+"
    required
>
```

HTML validation improves user experience, but server-side validation is still
required in real applications.

## 9. Accessibility

Accessibility means building pages that can be used by people with different
abilities, devices, and assistive technologies.

### Accessibility Checklist

- Set the document language with `lang`.
- Use semantic elements.
- Keep heading order logical.
- Add useful alt text to meaningful images.
- Connect labels to form controls.
- Use buttons for actions and links for navigation.
- Make link text descriptive.
- Do not rely only on color to communicate meaning.
- Keep interactive elements keyboard accessible.

### Good Link Text

Poor:

```html
<a href="report.pdf">Click here</a>
```

Better:

```html
<a href="report.pdf">Download the annual report</a>
```

### Buttons vs Links

Use a link for navigation:

```html
<a href="projects.html">View Projects</a>
```

Use a button for an action:

```html
<button type="button">Open Menu</button>
```

### ARIA Basics

ARIA can add extra accessibility information, but native HTML should be the first
choice.

```html
<nav aria-label="Footer navigation">
    <a href="privacy.html">Privacy</a>
    <a href="terms.html">Terms</a>
</nav>
```

Do not add ARIA when a native HTML element already communicates the same meaning.

## 10. Metadata and SEO

Metadata helps browsers, search engines, and social platforms understand the
page.

### Useful Metadata

```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta
        name="description"
        content="A student portfolio with Java and web development projects."
    >
    <title>Sanjog Portfolio</title>
</head>
```

### Open Graph Metadata

```html
<meta property="og:title" content="Sanjog Portfolio">
<meta property="og:description" content="Java and web development projects.">
<meta property="og:type" content="website">
<meta property="og:image" content="assets/portfolio-preview.png">
```

### SEO Basics

- Use one clear `h1`.
- Write descriptive page titles.
- Add useful meta descriptions.
- Use semantic sections.
- Make links descriptive.
- Add alt text to meaningful images.
- Keep content readable and organized.

## 11. Connecting CSS and JavaScript

HTML works with CSS and JavaScript through `link` and `script` elements.

### Link CSS

```html
<link rel="stylesheet" href="styles.css">
```

Place CSS links in the `head`.

### Add JavaScript

```html
<script src="app.js" defer></script>
```

Use `defer` for scripts that should run after the HTML has been parsed.

### Classes and IDs

Use classes for styling and repeated behavior:

```html
<article class="project-card">
    <h3>Todo List App</h3>
    <p>A Java console app for managing tasks.</p>
</article>
```

Use IDs for unique page targets or JavaScript hooks:

```html
<section id="contact">
    <h2>Contact</h2>
</section>
```

### Data Attributes

Data attributes store custom information in HTML:

```html
<button type="button" data-filter="java">Show Java Projects</button>
```

JavaScript can read the value later.

## 12. Project Structure

A small static HTML project can use this structure:

```text
portfolio/
|-- index.html
|-- about.html
|-- projects.html
|-- contact.html
|-- assets/
|   |-- profile.jpg
|   |-- project-preview.png
|-- css/
|   |-- styles.css
|-- js/
|   |-- app.js
```

File naming tips:

- Use lowercase names.
- Use hyphens for multi-word files: `project-detail.html`.
- Keep assets in folders.
- Make the home page `index.html`.
- Use relative links between local pages.

## 13. Mini Projects

### Mini Project 1: Profile Page

Build `profile.html`.

Requirements:

- Valid HTML document structure.
- `header`, `main`, and `footer`.
- One profile image with useful alt text.
- About section.
- Skills list.
- Contact links for email and GitHub.

Starter:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Profile</title>
</head>
<body>
    <header>
        <h1>Student Name</h1>
        <p>Java and Web Development Learner</p>
    </header>

    <main>
        <section>
            <h2>About Me</h2>
            <p>Write a short introduction here.</p>
        </section>
    </main>

    <footer>
        <p>&copy; 2026 Student Name</p>
    </footer>
</body>
</html>
```

### Mini Project 2: Blog Article

Build `blog-post.html`.

Requirements:

- Article title.
- Author and publish date.
- At least three sections.
- One image with `figure` and `figcaption`.
- Related links in an `aside`.

### Mini Project 3: Registration Form

Build `registration.html`.

Requirements:

- Full name, email, password, date of birth.
- Radio buttons for learning level.
- Checkbox for terms.
- Select field for preferred course.
- Submit button.
- Required validation.

### Mini Project 4: Product Page

Build `product.html`.

Requirements:

- Product title and description.
- Product image.
- Features list.
- Pricing table.
- Order form.
- FAQ section.

## 14. Final Capstone: Portfolio Website

Create a complete `index.html` portfolio page.

### Required Sections

- Header with name and short role.
- Navigation with links to page sections.
- Hero or introduction section.
- About section.
- Skills section.
- Projects section with at least three project articles.
- Education section.
- Contact section with a form.
- Footer with copyright and social links.

### Required HTML Features

- Valid document structure.
- Semantic layout elements.
- At least one image with useful alt text.
- At least one list.
- At least one table.
- At least one form.
- Descriptive links.
- Useful metadata.
- Logical heading order.

### Capstone Starter

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta
        name="description"
        content="Student portfolio featuring Java and web development projects."
    >
    <title>Student Portfolio</title>
</head>
<body>
    <header>
        <h1>Student Name</h1>
        <p>Java Developer and Web Development Learner</p>

        <nav aria-label="Main navigation">
            <ul>
                <li><a href="#about">About</a></li>
                <li><a href="#skills">Skills</a></li>
                <li><a href="#projects">Projects</a></li>
                <li><a href="#education">Education</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section id="about">
            <h2>About</h2>
            <p>
                I am learning Java, backend development, and web development by
                building practical projects.
            </p>
        </section>

        <section id="skills">
            <h2>Skills</h2>
            <ul>
                <li>Java</li>
                <li>HTML</li>
                <li>CSS</li>
                <li>Spring Boot</li>
            </ul>
        </section>

        <section id="projects">
            <h2>Projects</h2>

            <article>
                <h3>Cab Booking System</h3>
                <p>
                    A Spring Boot project for ride booking, driver management,
                    and admin dashboards.
                </p>
                <a href="projects/cab-booking.html">Read about this project</a>
            </article>

            <article>
                <h3>Todo List Console App</h3>
                <p>A Java console app for managing tasks.</p>
            </article>

            <article>
                <h3>Expense Splitter</h3>
                <p>A Java program for splitting shared expenses.</p>
            </article>
        </section>

        <section id="education">
            <h2>Education</h2>
            <table>
                <caption>Education Summary</caption>
                <thead>
                    <tr>
                        <th scope="col">Program</th>
                        <th scope="col">Institution</th>
                        <th scope="col">Focus</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>BE Computer Engineering</td>
                        <td>Pokhara University</td>
                        <td>Software development</td>
                    </tr>
                </tbody>
            </table>
        </section>

        <section id="contact">
            <h2>Contact</h2>
            <form action="/contact" method="post">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" required>

                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>

                <label for="message">Message</label>
                <textarea id="message" name="message" rows="5" required></textarea>

                <button type="submit">Send Message</button>
            </form>
        </section>
    </main>

    <footer>
        <p>&copy; 2026 Student Name</p>
        <a href="mailto:student@example.com">student@example.com</a>
    </footer>
</body>
</html>
```

## 15. Debugging and Validation

Common mistakes:

- Missing closing tags.
- Incorrect nesting.
- Multiple `h1` elements without a clear reason.
- Images without useful alt text.
- Form inputs without labels.
- Links with empty or unclear text.
- Using tables for page layout.
- Using `div` for everything.

Manual checks:

1. Open the page in a browser.
2. Check the browser tab title.
3. Click every link.
4. Submit forms with missing fields to test validation.
5. Inspect the page in developer tools.
6. Navigate using only the keyboard.
7. Review heading order.
8. Validate the HTML with an HTML validator.

## 16. HTML Cheat Sheet

### Basic Template

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title</title>
</head>
<body>
    <h1>Main Heading</h1>
</body>
</html>
```

### Common Elements

```html
<h1>Main heading</h1>
<h2>Section heading</h2>
<p>Paragraph text.</p>
<a href="about.html">About</a>
<img src="photo.jpg" alt="Useful image description">
<ul>
    <li>List item</li>
</ul>
<button type="button">Button</button>
```

### Semantic Layout

```html
<header></header>
<nav></nav>
<main></main>
<section></section>
<article></article>
<aside></aside>
<footer></footer>
```

### Form Pattern

```html
<label for="email">Email</label>
<input type="email" id="email" name="email" required>
```

## 17. Next Steps After HTML

After finishing this course, continue with:

- CSS fundamentals for layout, spacing, colors, typography, and responsiveness.
- JavaScript fundamentals for interactivity.
- Git and GitHub for version control.
- Browser developer tools for debugging.
- Backend development with Java, Spring Boot, and databases.
