import React, { Component } from 'react';
import PropTypes from 'prop-types';

import sortBy from 'lodash/sortBy';

import TableHeader from 'grommet/components/TableHeader';
import Table from 'grommet/components/Table';
import TableRow from 'grommet/components/TableRow';
import Image from 'grommet/components/Image';

import placeholder from './assets/Placeholder.jpg';

class MedicinesList extends Component {
  constructor(props) {
    super(props);

    this.state = {
      medicines: props.medicinesList,
      fieldMapping: {
        CNK: 'cnk',
        Name: 'name',
        'Kind of Dosage': 'dosageKindDescription',
        'Active Ingredients': 'activeIngredients'
      },
      columns: ['Picture', 'CNK', 'Name', 'Kind of Dosage', 'Active Ingredients', 'In Stock'],
      sortIndex: null,
      sortAscending: null
    };
  }

  componentWillReceiveProps = nextProps => (
    this.setState({
      medicines: nextProps.medicinesList
    })
  );

  sortHandler = (index, ascending) => {
    const { medicines, columns } = this.state;

    const fieldName = columns[index];

    if (fieldName === 'Picture') {
      return;
    }

    const mappedField = this.state.fieldMapping[fieldName];

    const sortedMedicines = ascending ?
      sortBy(medicines, [mappedField]) :
      sortBy(medicines, [mappedField]).reverse();

    this.setState({
      medicines: sortedMedicines,
      sortIndex: index,
      sortAscending: ascending
    });
  };

  renderTableHeader = () => (
    <TableHeader
      labels={this.state.columns}
      sortIndex={this.state.sortIndex}
      sortAscending={this.state.sortAscending}
      onSort={this.sortHandler}
    />
  );

  renderTableItems = () => (
    <tbody>
      {
        this.state.medicines.map(medicine =>
          (
            <TableRow key={medicine.id}>
              <td>
                <Image
                  size='thumb'
                  src={placeholder}
                />
              </td>
              <td>{medicine.cnk}</td>
              <td>{medicine.name}</td>
              <td>{medicine.dosageKindDescription}</td>
              <td>{medicine.activeIngredients}</td>
              <td>Not Implemented</td>
            </TableRow>
          )
        )
      }
    </tbody>
  );

  render() {
    return (
      <Table
        selectable
      >
        {this.renderTableHeader()}
        {this.renderTableItems()}
      </Table>
    );
  }
}


export default MedicinesList;

MedicinesList.propTypes = {
  medicinesList: PropTypes.arrayOf(PropTypes.shape()).isRequired,
};
