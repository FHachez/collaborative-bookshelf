import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo';
import { IntlProvider, addLocaleData } from 'react-intl';
import { Provider } from 'react-redux';
import en from 'react-intl/locale-data/en';
import { getCurrentLocale, getLocaleData } from 'grommet/utils/Locale';

import Grommet from 'grommet/components/App';

import Routes from './routes';

import initalStore from './store';
import client from './apollo';


const locale = getCurrentLocale();
addLocaleData(en);
let messages;
try {
  messages = require(`../i18n/${locale}`);
} catch (e) {
  messages = require('../i18n/en-US');
}
const localeData = getLocaleData(messages, locale);

const App = () => (
  <ApolloProvider client={client}>
    <IntlProvider locale={localeData.locale} messages={localeData.messages}>
      <Grommet centered={false}>
        <Provider store={initalStore}>
          <Router>
            <Routes />
          </Router>
        </Provider>
      </Grommet>
    </IntlProvider>
  </ApolloProvider>
);

export default App;
