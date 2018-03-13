import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';

import Form from 'react-formify';

import Box from 'grommet/components/Box';
import FormFields from 'grommet/components/FormFields';
import FormField from 'grommet/components/FormField';
import TextInput from 'grommet/components/TextInput';
import PasswordInput from 'grommet/components/PasswordInput';
import Button from 'grommet/components/Button';
import Footer from 'grommet/components/Footer';
import Toast from 'grommet/components/Toast';

import { sendForm } from './data/login-actions';

class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      loginErrorShown: true,
      rules: {
        email: (value) => {
          if (!value || value === '') {
            return 'Email is required';
          } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
            return 'Please provide a valid email';
          }
          return undefined;
        },
        password: (value) => {
          if (!value || value === '') {
            return 'Password is required';
          }
          return undefined;
        }
      }
    };
  }

  setLoginErrorView = state => (
    () => (
      this.setState({ loginErrorShown: state })
    )
  )

  emailInput = (field, error) => (
    <FormField label='Email' error={error}>
      <TextInput placeHolder='Email' {...field} onDOMChange={field.onChange} />
    </FormField>
  );

  passwordInput = (field, error) => (
    <FormField label='Password' error={error}>
      <PasswordInput placeHolder='Password' {...field} />
    </FormField>
  )

  handleFormSubmit = (form) => {
    this.setLoginErrorView(true)();

    this.props.sendForm(form);
  }

  loginErrorView = () => {
    if (this.state.loginErrorShown && this.props.loginStore && this.props.loginStore.attempts > 0) {
      return (
        <Toast
          onClose={this.setLoginErrorView(false)}
          status='warning'
          size='small'
        >
          Wrong credentials! Try again :)
        </Toast>
      );
    }

    return <div />;
  }

  loginForm = () => (
    <Form
      onSubmit={this.handleFormSubmit}
      rules={this.state.rules}
    >
      {
        (state, errors) => (
          <div>
            <FormFields>
              {this.emailInput(state.email, errors.email)}
              {this.passwordInput(state.password, errors.password)}
            </FormFields>
            <Footer pad={{ vertical: 'medium' }}>
              <Button
                label='Submit'
                type='submit'
                fill
                primary
              />
            </Footer>
          </div>
        )
      }
    </Form>
  );

  render() {
    if (this.props.loginStore && this.props.loginStore.jwt) {
      return (
        <Redirect to='/' />
      );
    }

    return (
      <Box
        full='vertical'
        align='center'
        justify='center'
        flex='grow'
        pad='medium'
        direction='row'
      >
        <Box basis='medium'>
          {this.loginErrorView()}
          {this.loginForm()}
        </Box>
      </Box>
    );
  }
}

const mapStateToProps = state => (
  {
    loginStore: state.pages.login.store
  }
);

export default connect(
  mapStateToProps,
  { sendForm },
)(Login);

Login.propTypes = {

  loginStore: PropTypes.shape({
    jwt: PropTypes.string,
    attempts: PropTypes.number,
  }).isRequired,
  sendForm: PropTypes.func.isRequired
};
