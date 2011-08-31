class AddStatusToMessages < ActiveRecord::Migration
  def self.up
    remove_column :messages, :hash
    add_column :messages, :status, :int
  end

  def self.down
    add_column :messages, :hash, :string
    remove_column :messages, :status
  end
end
