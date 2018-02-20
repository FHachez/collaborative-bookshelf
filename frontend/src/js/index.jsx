import 'whatwg-fetch';
import { polyfill as promisePolyfill } from 'es6-promise';

import React from 'react';
import ReactDOM from 'react-dom';

import '../scss/index.scss';

import App from './app/App';

promisePolyfill();

ReactDOM.render(
  <App />,
  document.getElementById('content')
);

document.body.classList.remove('loading');
